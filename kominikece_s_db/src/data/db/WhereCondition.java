package data.db;

public class WhereCondition {
    private String oterator, value, column, operationOperator;

    public WhereCondition(String oterator, String value, String column, String operationOperator) {
        this.oterator = oterator;
        this.value = value;
        this.column = column;
        this.operationOperator = operationOperator;
    }

    public String getCondition() {
        return " " + this.oterator + " " + column + " " + operationOperator + " " + value + " "; 
    }

    public enum Operator {
        AND("AND"), OR("OR"), NOT("NOT"), NON("");
        
        private String operator;

        private Operator(String operator) {
            this.operator = operator;
        }

        @Override
        public String toString() {
            return operator;
        }
    }

    public enum OperationOperator {
        EQUAL("="), GREATER(">"), LESS("<"), NOTEQUAL("<>"), BETWEEN("BETWEEN"), LIKE("LIKE"), IN("IN");

        private String operationOperator;

        private OperationOperator(String operationOperator) {
            this.operationOperator = operationOperator;
        }

        @Override
        public String toString() {
            return operationOperator;
        }
    }
}
