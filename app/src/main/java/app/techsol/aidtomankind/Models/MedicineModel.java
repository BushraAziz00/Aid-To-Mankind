package app.techsol.aidtomankind.Models;

public class MedicineModel {
    String id;
    String name;

    public MedicineModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    String quantity;
    String type;
    String formula;
    String info;
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public MedicineModel(String id, String name, String quantity, String type, String formula, String info, String price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.formula = formula;
        this.info = info;
        this.price = price;
    }
}
