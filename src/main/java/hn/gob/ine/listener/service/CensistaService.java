package hn.gob.ine.listener.service;

import hn.gob.ine.listener.model.CobCondicionVivienda;
import hn.gob.ine.listener.dao.ControlCorteDAO;
import hn.gob.ine.listener.dao.DestinoMonitoreoDAO;
import hn.gob.ine.listener.dao.OrigenCnpvDAO;
import hn.gob.ine.listener.model.CalidadCompHogEnvejecimiento;
import hn.gob.ine.listener.model.CalidadCompHogPiramide;
import hn.gob.ine.listener.model.CalidadConyEstado;
import hn.gob.ine.listener.model.CalidadEduGrupoEdad;
import hn.gob.ine.listener.model.CalidadEduNivelEducativo;
import hn.gob.ine.listener.model.CobArea;
import hn.gob.ine.listener.model.CobCoberturaDepartamentos;
import hn.gob.ine.listener.model.LlaveCensista;
import hn.gob.ine.listener.model.TotalHogViv;
import hn.gob.ine.listener.model.CobEvolucionCobertura;
import hn.gob.ine.listener.model.CobTotalViviendasDesocupada;
import hn.gob.ine.listener.model.GeVivCobViviendasCensadas;
import hn.gob.ine.listener.model.GeVivCobHogaresCensados;
import hn.gob.ine.listener.model.GeVivCobTipoViv;
import hn.gob.ine.listener.model.CobTotalViviendasOcupadas;
import hn.gob.ine.listener.model.CobTotalViviendasOcupAusentes;
import hn.gob.ine.listener.model.GeVivCobCensadasSin;
import hn.gob.ine.listener.model.CobTotalViviendasParticulares;
import hn.gob.ine.listener.model.GeVivCobDeptosCompletados;
import hn.gob.ine.listener.model.GeVivCobMunisCompletados;
import hn.gob.ine.listener.model.CalidadVivVivienda;
import hn.gob.ine.listener.model.CalidadVivHogar;
import hn.gob.ine.listener.model.CalidadHogServicios;
import hn.gob.ine.listener.model.CalidadHogNucleo;
import hn.gob.ine.listener.model.CalidadHogHacinamiento;
import hn.gob.ine.listener.model.CalidadDiscLimitacion;
import hn.gob.ine.listener.model.CalidadEtnIndigenaAfro;
import hn.gob.ine.listener.model.CalidadFecFecundidad;
import hn.gob.ine.listener.model.CalidadMigEmigranteGenero;
import hn.gob.ine.listener.model.CalidadMigEmigrantePaisDest;
import hn.gob.ine.listener.model.CalidadMorMortalidad;
import hn.gob.ine.listener.model.CalidadNacResOtroLugar;
import hn.gob.ine.listener.model.CalidadTicAcceso;
import hn.gob.ine.listener.model.CalidadTraOcuOcupacion;
import hn.gob.ine.listener.model.CobCensistaProductividad;
import hn.gob.ine.listener.model.CobCensistaPorVivienda;

import java.util.ArrayList;
import java.util.List;

public class CensistaService {

    private ControlCorteDAO controlDAO;
    private OrigenCnpvDAO origenDAO;
    private DestinoMonitoreoDAO destinoDAO;

    public CensistaService() {
        this.controlDAO = new ControlCorteDAO();
        this.origenDAO = new OrigenCnpvDAO();
        this.destinoDAO = new DestinoMonitoreoDAO();
    }

    @FunctionalInterface
    private interface PasoCorte {
        void ejecutar() throws Exception;
    }

    public void procesarCensista(int idEjecucion, LlaveCensista llave) {

        try {
            if (controlDAO.censistaFinalizado(idEjecucion, llave)) {
                System.out.println("Ya estaba finalizado: " + llave);
                return;
            }

            controlDAO.marcarProcesando(idEjecucion, llave);

            System.out.println("Procesando censista: " + llave);

            List<String> errores = new ArrayList<String>();

            TotalHogViv[] totalHolder = new TotalHogViv[1];

            ejecutarPaso(idEjecucion, llave, "total_hog_viv", errores, () -> {
                TotalHogViv total = origenDAO.calcularTotalHogViv(llave);
                destinoDAO.refrescarTotalHogViv(total);
                totalHolder[0] = total;
                System.out.println("Insertado total_hog_viv -> hogar: " + total.getHogar()
                        + ", vivienda: " + total.getVivienda()
                        + ", personas: " + total.getCantidadPersonas());
            });

            ejecutarPaso(idEjecucion, llave, "cob_condicion_vivienda", errores, () -> {
                CobCondicionVivienda cob = origenDAO.calcularCobCondicionVivienda(llave);
                destinoDAO.refrescarCobCondicionVivienda(cob);
                System.out.println("Insertado cob_condicion_vivienda -> ocupadas: "
                        + cob.getOcupadasPresentes()
                        + ", rechazadas: " + cob.getRechazadas()
                        + ", pendientes: " + cob.getPendientes());
            });

            ejecutarPaso(idEjecucion, llave, "cob_area", errores, () -> {
                CobArea cobArea = origenDAO.calcularCobArea(llave);
                destinoDAO.refrescarCobArea(cobArea);
                System.out.println("Insertado cob_area -> urbana: "
                        + cobArea.getAreaUrbana()
                        + ", rural: " + cobArea.getAreaRural());
            });

            ejecutarPaso(idEjecucion, llave, "cob_evolucion_cobertura", errores, () -> {
                List<CobEvolucionCobertura> evolucion = origenDAO.calcularCobEvolucionCobertura(llave);
                destinoDAO.refrescarCobEvolucionCobertura(evolucion);
                System.out.println("Insertado cob_evolucion_cobertura -> registros fecha: " + evolucion.size());
            });

            if (totalHolder[0] != null) {
                ejecutarPaso(idEjecucion, llave, "ge-viv-cob-viviendas-censadas / ge-viv-cob-hogares-censadas", errores, () -> {
                    TotalHogViv total = totalHolder[0];

                    GeVivCobViviendasCensadas vivCensadas = new GeVivCobViviendasCensadas(
                            total.getDepto(), total.getMuni(), total.getApoyoMunicipal(), total.getZona(), String.valueOf(total.getSector()),
                            total.getSegmento(), total.getCensista(), total.getVivienda()
                    );
                    destinoDAO.refrescarGeVivCobViviendasCensadas(vivCensadas);

                    GeVivCobHogaresCensados hogCensados = new GeVivCobHogaresCensados(
                            total.getDepto(), total.getMuni(), total.getApoyoMunicipal(), total.getZona(), String.valueOf(total.getSector()),
                            total.getSegmento(), total.getCensista(), total.getHogar()
                    );
                    destinoDAO.refrescarGeVivCobHogaresCensados(hogCensados);

                    System.out.println("Insertado ge-viv-cob -> viviendas: "
                            + total.getVivienda() + ", hogares: " + total.getHogar());
                });
            }

            ejecutarPaso(idEjecucion, llave, "ge-viv-cob-tipo-viv", errores, () -> {
                GeVivCobTipoViv tipoViv = origenDAO.calcularGeVivCobTipoViv(llave);
                destinoDAO.refrescarGeVivCobTipoViv(tipoViv);
                System.out.println("Insertado ge-viv-cob-tipo-viv -> particular: "
                        + tipoViv.getVivParticular()
                        + ", apartamento: " + tipoViv.getApartamento()
                        + ", cuarteria: " + tipoViv.getCuarteria()
                        + ", otro: " + tipoViv.getOtroTipo());
            });

            ejecutarPaso(idEjecucion, llave, "cob_total_viviendas_ocupadas", errores, () -> {
                CobTotalViviendasOcupadas ocupadas = origenDAO.calcularCobTotalViviendasOcupadas(llave);
                destinoDAO.refrescarCobTotalViviendasOcupadas(ocupadas);
                System.out.println("Insertado cob_total_viviendas_ocupadas -> ocupadas: "
                        + ocupadas.getCantidadOcupadas() + ", cantidad: " + ocupadas.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "cob_total_viviendas_desocupada", errores, () -> {
                CobTotalViviendasDesocupada desocupada = origenDAO.calcularCobTotalViviendasDesocupada(llave);
                destinoDAO.refrescarCobTotalViviendasDesocupada(desocupada);
                System.out.println("Insertado cob_total_viviendas_desocupada -> desocupadas: "
                        + desocupada.getCantidadDesocupada() + ", cantidad: " + desocupada.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "cob_total_viviendas_ocup_ausentes", errores, () -> {
                CobTotalViviendasOcupAusentes ocupAusentes = origenDAO.calcularCobTotalViviendasOcupAusentes(llave);
                destinoDAO.refrescarCobTotalViviendasOcupAusentes(ocupAusentes);
                System.out.println("Insertado cob_total_viviendas_ocup_ausentes -> ausentes: "
                        + ocupAusentes.getCantidadOcupadasAusente() + ", cantidad: " + ocupAusentes.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "cob_total_viviendas_particulares", errores, () -> {
                CobTotalViviendasParticulares particulares = origenDAO.calcularCobTotalViviendasParticulares(llave);
                destinoDAO.refrescarCobTotalViviendasParticulares(particulares);
                System.out.println("Insertado cob_total_viviendas_particulares -> particulares: "
                        + particulares.getCantidadParticulares() + ", cantidad: " + particulares.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "ge-viv-cob-censadas-sin", errores, () -> {
                GeVivCobCensadasSin censadasSin = origenDAO.calcularGeVivCobCensadasSin(llave);
                destinoDAO.refrescarGeVivCobCensadasSin(censadasSin);
                System.out.println("Insertado ge-viv-cob-censadas-sin -> agua: "
                        + censadasSin.getAgua()
                        + ", energia: " + censadasSin.getEnergia()
                        + ", sanitario: " + censadasSin.getSanitario());
            });

            ejecutarPaso(idEjecucion, llave, "cob_cobertura_departamentos", errores, () -> {
                CobCoberturaDepartamentos cobDepto = origenDAO.calcularCobCoberturaDepartamentos(llave.getDepto());
                destinoDAO.refrescarCobCoberturaDepartamentos(cobDepto);
                System.out.println("Insertado cob_cobertura_departamentos -> realizado: "
                        + cobDepto.getRealizado() + ", por_realizar: " + cobDepto.getPorRealizar());
            });

            ejecutarPaso(idEjecucion, llave, "ge-viv-cob-deptos-completados", errores, () -> {
                GeVivCobDeptosCompletados deptosCompletados = origenDAO.calcularGeVivCobDeptosCompletados(llave.getDepto());
                destinoDAO.refrescarGeVivCobDeptosCompletados(deptosCompletados);
                System.out.println("Insertado ge-viv-cob-deptos-completados -> completado: "
                        + deptosCompletados.getCompletado());
            });

            ejecutarPaso(idEjecucion, llave, "ge-viv-cob-munis-completados", errores, () -> {
                GeVivCobMunisCompletados munisCompletados = origenDAO.calcularGeVivCobMunisCompletados(llave.getDepto(), llave.getMuni());
                destinoDAO.refrescarGeVivCobMunisCompletados(munisCompletados);
                System.out.println("Insertado ge-viv-cob-munis-completados -> completado: "
                        + munisCompletados.getCompletado());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_viv_vivienda", errores, () -> {
                CalidadVivVivienda calVivVivienda = origenDAO.calcularCalidadVivVivienda(llave);
                destinoDAO.refrescarCalidadVivVivienda(calVivVivienda);
                System.out.println("Insertado calidad_viv_vivienda -> cantidad: " + calVivVivienda.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_viv_hogar", errores, () -> {
                CalidadVivHogar calVivHogar = origenDAO.calcularCalidadVivHogar(llave);
                destinoDAO.refrescarCalidadVivHogar(calVivHogar);
                System.out.println("Insertado calidad_viv_hogar -> cantidad: " + calVivHogar.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_hog_servicios", errores, () -> {
                CalidadHogServicios calHogServicios = origenDAO.calcularCalidadHogServicios(llave);
                destinoDAO.refrescarCalidadHogServicios(calHogServicios);
                System.out.println("Insertado calidad_hog_servicios -> cantidad: " + calHogServicios.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_hog_nucleo", errores, () -> {
                CalidadHogNucleo calHogNucleo = origenDAO.calcularCalidadHogNucleo(llave);
                destinoDAO.refrescarCalidadHogNucleo(calHogNucleo);
                System.out.println("Insertado calidad_hog_nucleo -> jefe_mujer: "
                        + calHogNucleo.getJefeMujer() + ", jefe_hombre: " + calHogNucleo.getJefeHombre());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_hog_hacinamiento", errores, () -> {
                CalidadHogHacinamiento calHogHacinamiento = origenDAO.calcularCalidadHogHacinamiento(llave);
                destinoDAO.refrescarCalidadHogHacinamiento(calHogHacinamiento);
                System.out.println("Insertado calidad_hog_hacinamiento -> mayor_3_hac: "
                        + calHogHacinamiento.getMayor3Hac() + ", num_personas: " + calHogHacinamiento.getNumPersonas());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_edu_grupo_edad", errores, () -> {
                CalidadEduGrupoEdad calEduGrupoEdad = origenDAO.calcularCalidadEduGrupoEdad(llave);
                destinoDAO.refrescarCalidadEduGrupoEdad(calEduGrupoEdad);
                System.out.println("Insertado calidad_edu_grupo_edad -> poblacion_5_17: " + calEduGrupoEdad.getPoblacion5_17());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_edu_nivel_educativo", errores, () -> {
                CalidadEduNivelEducativo calEduNivel = origenDAO.calcularCalidadEduNivelEducativo(llave);
                destinoDAO.refrescarCalidadEduNivelEducativo(calEduNivel);
                System.out.println("Insertado calidad_edu_nivel_educativo -> poblacion_15_mas: " + calEduNivel.getPoblacion15Mas());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_disc_limitacion", errores, () -> {
                CalidadDiscLimitacion calDiscLimitacion = origenDAO.calcularCalidadDiscLimitacion(llave);
                destinoDAO.refrescarCalidadDiscLimitacion(calDiscLimitacion);
                System.out.println("Insertado calidad_disc_limitacion -> cant_limitacion: " + calDiscLimitacion.getCantLimitacion());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_etn_indigena_afro", errores, () -> {
                CalidadEtnIndigenaAfro calEtnIndigenaAfro = origenDAO.calcularCalidadEtnIndigenaAfro(llave);
                destinoDAO.refrescarCalidadEtnIndigenaAfro(calEtnIndigenaAfro);
                System.out.println("Insertado calidad_etn_indigena_afro -> indigena: "
                        + calEtnIndigenaAfro.getCantIndigena() + ", afro: " + calEtnIndigenaAfro.getCantAfro());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_mig_emigrante_genero", errores, () -> {
                CalidadMigEmigranteGenero calMigGenero = origenDAO.calcularCalidadMigEmigranteGenero(llave);
                destinoDAO.refrescarCalidadMigEmigranteGenero(calMigGenero);
                System.out.println("Insertado calidad_mig_emigrante_genero -> mujeres: "
                        + calMigGenero.getCantMujerEmi() + ", hombres: " + calMigGenero.getCantHombreEmi());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_mig_emigrante_pais_dest", errores, () -> {
                CalidadMigEmigrantePaisDest calMigPaisDest = origenDAO.calcularCalidadMigEmigrantePaisDest(llave);
                destinoDAO.refrescarCalidadMigEmigrantePaisDest(calMigPaisDest);
                System.out.println("Insertado calidad_mig_emigrante_pais_dest -> cantidad: " + calMigPaisDest.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_mor_mortalidad", errores, () -> {
                CalidadMorMortalidad calMorMortalidad = origenDAO.calcularCalidadMorMortalidad(llave);
                destinoDAO.refrescarCalidadMorMortalidad(calMorMortalidad);
                System.out.println("Insertado calidad_mor_mortalidad -> cantidad: " + calMorMortalidad.getCantidad());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_fec_fecundidad", errores, () -> {
                CalidadFecFecundidad calFecFecundidad = origenDAO.calcularCalidadFecFecundidad(llave);
                destinoDAO.refrescarCalidadFecFecundidad(calFecFecundidad);
                System.out.println("Insertado calidad_fec_fecundidad -> total_mujeres: " + calFecFecundidad.getTotalMujeres());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_tra_ocu_ocupacion", errores, () -> {
                CalidadTraOcuOcupacion calTraOcuOcupacion = origenDAO.calcularCalidadTraOcuOcupacion(llave);
                destinoDAO.refrescarCalidadTraOcuOcupacion(calTraOcuOcupacion);
                System.out.println("Insertado calidad_tra_ocu_ocupacion -> ocupada_h: "
                        + calTraOcuOcupacion.getCantPOcupadaH() + ", ocupada_m: " + calTraOcuOcupacion.getCantPOcupadaM());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_tic_acceso", errores, () -> {
                CalidadTicAcceso calTicAcceso = origenDAO.calcularCalidadTicAcceso(llave);
                destinoDAO.refrescarCalidadTicAcceso(calTicAcceso);
                System.out.println("Insertado calidad_tic_acceso -> poblacion_total: " + calTicAcceso.getPoblacionTotal());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_nac_res_otro_lugar", errores, () -> {
                CalidadNacResOtroLugar calNacResOtroLugar = origenDAO.calcularCalidadNacResOtroLugar(llave);
                destinoDAO.refrescarCalidadNacResOtroLugar(calNacResOtroLugar);
                System.out.println("Insertado calidad_nac_res_otro_lugar -> otro_muni: "
                        + calNacResOtroLugar.getCantNacOtroMuni() + ", otro_pais: " + calNacResOtroLugar.getCantNacOtroPais());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_comp_hog_envejecimiento", errores, () -> {
                CalidadCompHogEnvejecimiento calCompHogEnvejecimiento = origenDAO.calcularCalidadCompHogEnvejecimiento(llave);
                destinoDAO.refrescarCalidadCompHogEnvejecimiento(calCompHogEnvejecimiento);
                System.out.println("Insertado calidad_comp_hog_envejecimiento -> mayor_60_h: "
                        + calCompHogEnvejecimiento.getCantMayor60H() + ", mayor_60_m: " + calCompHogEnvejecimiento.getCantMayor60M());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_comp_hog_piramide", errores, () -> {
                CalidadCompHogPiramide calCompHogPiramide = origenDAO.calcularCalidadCompHogPiramide(llave);
                destinoDAO.refrescarCalidadCompHogPiramide(calCompHogPiramide);
                System.out.println("Insertado calidad_comp_hog_piramide -> 100_mas_h: " + calCompHogPiramide.getEdad100MasH());
            });

            ejecutarPaso(idEjecucion, llave, "cob_censista_productividad", errores, () -> {
                CobCensistaProductividad calProductividad = origenDAO.calcularCobCensistaProductividad(llave);
                destinoDAO.refrescarCobCensistaProductividad(calProductividad);
                System.out.println("Insertado cob_censista_productividad -> estructuras: "
                        + calProductividad.getEstructurasTrabajadas()
                        + ", cuestionarios_efectivos: " + calProductividad.getCuestionariosEfectivos());
            });

            ejecutarPaso(idEjecucion, llave, "cob_censista_por_vivienda", errores, () -> {
                CobCensistaPorVivienda calPorVivienda = origenDAO.calcularCobCensistaPorVivienda(llave);
                destinoDAO.refrescarCobCensistaPorVivienda(calPorVivienda);
                System.out.println("Insertado cob_censista_por_vivienda -> particulares: "
                        + calPorVivienda.getTotalViviendasParticulares()
                        + ", efectivas: " + calPorVivienda.getBoletasEfectivas());
            });

            ejecutarPaso(idEjecucion, llave, "calidad_cony_estado", errores, () -> {
                CalidadConyEstado calConyEstado = origenDAO.calcularCalidadConyEstado(llave);
                destinoDAO.refrescarCalidadConyEstado(calConyEstado);
                System.out.println("Insertado calidad_cony_estado -> casado_h: "
                        + calConyEstado.getCantCasadoH() + ", soltero_h: " + calConyEstado.getCantSolteroH());
            });

            if (errores.isEmpty()) {
                controlDAO.marcarFinalizado(idEjecucion, llave);
                System.out.println("Finalizado censista: " + llave);
            } else {
                String resumen = errores.size() + " paso(s) fallaron: " + String.join(" | ", errores);
                controlDAO.marcarError(idEjecucion, llave, resumen);
                System.out.println("Finalizado censista con errores parciales (" + errores.size() + "): " + llave);
            }

        } catch (Exception e) {
            try {
                controlDAO.marcarError(idEjecucion, llave, e.getMessage());
            } catch (Exception ex) {
                System.err.println("No se pudo marcar error en control:");
                ex.printStackTrace();
            }

            System.err.println("Error procesando censista: " + llave);
            e.printStackTrace();
        }
    }

    private void ejecutarPaso(int idEjecucion, LlaveCensista llave, String tablaDestino, List<String> errores, PasoCorte paso) {

        try {
            paso.ejecutar();
        } catch (Exception e) {
            String mensaje = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            errores.add(tablaDestino + ": " + mensaje);

            System.err.println("Error en paso '" + tablaDestino + "' para censista " + llave + ": " + mensaje);

            try {
                controlDAO.registrarErrorPaso(idEjecucion, llave, tablaDestino, mensaje);
            } catch (Exception ex) {
                System.err.println("No se pudo registrar en log_error_corte para '" + tablaDestino + "':");
                ex.printStackTrace();
            }
        }
    }

}
