package com.samagra.cascading_module.models;

/**
 * A POJO for representing the a InstitutionInfo.
 */
public class InstitutionInfo {

    public String District;
    public String Block;
    public String Category;
    public String Institution;

    public String getStringForSearch() {
        return this.District + " "
                + this.Block + " "
                + this.Category + " "
                + this.Institution;
    }

    public InstitutionInfo(String district, String block, String institutionType, String institutionName) {
        this.Institution = institutionName;
        this.Block = block;
        this.Category = institutionType;
        this.District = district;
    }

    public InstitutionInfo(String district, String block, String cluster, String institutionType, String institutionName) {
        this.Institution = institutionName;
        this.Block = block;
        this.Category = institutionType;
        this.District = district;
    }

}
