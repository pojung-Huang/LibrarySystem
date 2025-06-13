package tw.ispan.librarysystem.dto;

public class SearchCondition {
    private String field;
    private String operator; // AND, OR, NOT
    private String value;

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
} 