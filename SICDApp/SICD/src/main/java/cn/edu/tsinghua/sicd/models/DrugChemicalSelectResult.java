package cn.edu.tsinghua.sicd.models;

/**
 * Created by douglaschan on 2016/3/22.
 */
public class DrugChemicalSelectResult {
    public String DrugOrChemicalProduct;

    public String getPoisoning() {
        return Poisoning;
    }

    public void setPoisoning(String poisoning) {
        Poisoning = poisoning;
    }

    public String Poisoning;

    public String getDrugOrChemicalProduct() {
        return DrugOrChemicalProduct;
    }

    public void setDrugOrChemicalProduct(String drugOrChemicalProduct) {
        DrugOrChemicalProduct = drugOrChemicalProduct;
    }


    public String getAccident() {
        return Accident;
    }

    public void setAccident(String accident) {
        Accident = accident;
    }

    public String getIntentionalSelfHarm() {
        return IntentionalSelfHarm;
    }

    public void setIntentionalSelfHarm(String intentionalSelfHarm) {
        IntentionalSelfHarm = intentionalSelfHarm;
    }

    public String getUnknownIntent() {
        return UnknownIntent;
    }

    public void setUnknownIntent(String unknownIntent) {
        UnknownIntent = unknownIntent;
    }

    public String getAdvesreEffect() {
        return AdvesreEffect;
    }

    public void setAdvesreEffect(String advesreEffect) {
        AdvesreEffect = advesreEffect;
    }

    public String Accident;
    public String IntentionalSelfHarm;
    public String UnknownIntent;
    public String AdvesreEffect;
}
