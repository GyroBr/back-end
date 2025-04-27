package com.Gyro.back_end_gyro.domain.company.enums;

public enum Sector {

    RESTAURANTES,
    BARES_E_PUBS,
    CAFETERIAS_E_PADARIAS,
    LANCHONETES,
    EMPORIOS_E_MERCEARIAS,
    SUPERMERCADOS_E_HIPERMERCADOS,
    ADEGAS_E_VINICOLAS,
    INDUSTRIAS_DE_BEBIDAS,
    DISTRIBUIDORAS_DE_ALIMENTOS_E_BEBIDAS,
    FOOD_TRUCKS,
    EVENTOS_E_CATERING,
    MERCADOS_DE_ALIMENTOS_ORGANICOS,
    FABRICAS_DE_PRODUTOS_ALIMENTICIOS,
    CLUBES_E_ASSOCIACOES_DE_BEBIDAS,
    COMERCIOS_DE_PRODUTOS_GOURMET;

    public static Sector findSector(String sector) throws IllegalAccessException {
        for (Sector sIndex : Sector.values()) {
            if (sIndex.name().replace("_", " ").equals(sector)) {
                return sIndex;
            }
        }

        throw new IllegalAccessException("Setor inválido");

    }
}
