package com.tsystems.javaschool.tasks.calculator;

public class Calculator {

    private enum OperationState {
        MULTIPLICATION_OR_DIVISION,
        ADDITION_OR_SUBSTRACTION
    }
    private enum ExpressionReadState{
        READING_OPERAND,
        READING_OPERATION
    }
    private String[] expression;
    private String[] queue;
    private int queueRearPointer = 0;
    private int queueFrontPointer = 0;
    private String queueLastOperation = "+";

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        // TODO: Implement the logic here
        if(statement == null || statement.equals(""))
            return null;
        expression = statement.split("(?<=[+\\-=*/()])|(?=[+\\-=*/()])");
        OperationState operationState = OperationState.ADDITION_OR_SUBSTRACTION;
        ExpressionReadState readState = ExpressionReadState.READING_OPERAND;
        queue = new String[50];
        Double solvedQueue;
        String prevStateOperation;
        String prevReadedOperand = "0";
        Double result = 0.0;
        if(expression.length < 2){
            return expression[0];
        }
        else if(expression[1].equals("*") || expression[1].equals("/")){
            // if first operation is multiplication or division then
            // change operation state to MULTIPLICATION_OR_DIVISION
            readState = ExpressionReadState.READING_OPERAND;
            operationState = OperationState.MULTIPLICATION_OR_DIVISION;
        }
        for(int i = 0; i < expression.length; i++){
            if(readState == ExpressionReadState.READING_OPERAND){
                if(Character.isDigit(expression[i].charAt(0))){
                    prevReadedOperand = expression[i];
                }
                else if(expression[i].charAt(0) == '('){
                    Calculator c = new Calculator();
                    Integer openingBracketIndex = findFirstOpeningBracket(statement);
                    Integer closingBracketIndex = findClosingBracketStatementIndex(statement, openingBracketIndex);
                    if(closingBracketIndex != null) {
                        prevReadedOperand = c.evaluate(statement.substring(openingBracketIndex+1, closingBracketIndex));
                        if(prevReadedOperand == null){
                            return null;
                        }
                        i = findExpressionClosingBracketIndex(expression, i);
                    }
                    else{
                        return null;
                    }
                }
                else return null;
                readState = ExpressionReadState.READING_OPERATION;
            }
            else {
                if(didStateChanged(expression[i], operationState)){
                    operationState = changeState(operationState);
                    if(operationState == OperationState.ADDITION_OR_SUBSTRACTION) {
                        enQueue(prevReadedOperand);
                        enQueue(expression[i]);
                        prevStateOperation = queueLastOperation;
                        solvedQueue = evaluateQueue();
                        enQueue(Double.toString(result));
                        enQueue(prevStateOperation);
                        enQueue(Double.toString(solvedQueue));
                        enQueue(queueLastOperation);
                    }
                    else{
                        result = evaluateQueue();
                        enQueue(prevReadedOperand);
                        enQueue(expression[i]);
                    }
                }
                else{
                    enQueue(prevReadedOperand);
                    enQueue(expression[i]);
                }
                readState = ExpressionReadState.READING_OPERAND;
            }
        }
        enQueue(prevReadedOperand);
        if(operationState == OperationState.MULTIPLICATION_OR_DIVISION) {
            prevStateOperation = queueLastOperation;
            enQueue("");
            solvedQueue = evaluateQueue();
            if(solvedQueue == null)
                return null;
            enQueue(Double.toString(result));
            enQueue(prevStateOperation);
            enQueue(Double.toString(solvedQueue));
        }
        result = evaluateQueue();

        if(result == null){
            return null;
        }
        /*else
            result += solvedQueue;*/
        return String.format("%.5f", result).replaceAll("0+$", "").replaceAll("[.]$", "");
    }

    /**
     *
     * @param operation Current operation. Possible values are "*", "/", "+", "-"
     * @param operationState State before operation. Possible values are ADDITION_OR_SUBSTRACTION, MULTIPLICATION_OR_DIVISION
     * @return Returns true if operation type has changed.
     */
    private boolean didStateChanged(String operation, OperationState operationState){
        if(operationState == OperationState.ADDITION_OR_SUBSTRACTION){
            if(operation.equals("*")||operation.equals("/")){
                return true;
            }
        }
        else{
            if(operation.equals("+")||operation.equals("-")){
                return true;
            }
        }
        return false;
    }
    private OperationState changeState(OperationState operationState){
        return operationState == OperationState.ADDITION_OR_SUBSTRACTION ? OperationState.MULTIPLICATION_OR_DIVISION : OperationState.ADDITION_OR_SUBSTRACTION;
    }
    private String deQueue(){
        if(queueRearPointer<queue.length)
            return queue[queueRearPointer++];
        else
            return null;
    }
    private void enQueue(String value){
        queue[queueFrontPointer++] = value;
    }
    private Double evaluateQueue(){
        Double acc = 0.0;
        Double operand = 0.0;
        //String operation = queueLastOperation;
        String operation = "+";
        while(!isQueueEmpty()){
            operand = tryParseDouble(deQueue());
            if(operand == null)
                return null;
            else if(operation.equals("+")){
                acc += operand;
                operation = deQueue();
            }
            else if(operation.equals("-")){
                acc -= operand;
                operation = deQueue();
            }
            else if(operation.equals("*")){
                acc*= operand;
                operation = deQueue();
            }
            else if(operation.equals("/")){
                if(operand==0) {
                    return null;
                }
                else{
                    acc/=operand;
                    operation = deQueue();
                }
            }
        }
        queueLastOperation = operation;
        return acc;
    }
    private boolean isQueueEmpty(){
        return queueFrontPointer <= queueRearPointer;
    }
    private Double tryParseDouble(String str){
        try{
            return Double.parseDouble(str);
        }
        catch (NumberFormatException e){
            return null;
        }
    }
    private Integer findClosingBracketStatementIndex(String statement, int startIndex){
        int openingBracketCount = 0;
        int curIndex = startIndex;
        do{
            if(statement.charAt(curIndex) == '('){
                openingBracketCount++;
            }
            else if(statement.charAt(curIndex) == ')'){
                openingBracketCount--;
            }
            curIndex++;
        } while(openingBracketCount!=0 && curIndex < statement.length());
        if(curIndex<statement.length()){
            return --curIndex;
        }
        else{
            return null;
        }
    }
    private Integer findFirstOpeningBracket(String statement){
        int curIndex = 0;
        while(statement.charAt(curIndex) != '('){
            curIndex++;
        }
        return curIndex;
    }
    private Integer findExpressionClosingBracketIndex(String[] expression, int startIndex){
        int openingBracketCount = 0;
        int curIndex = startIndex;
        do{
            if(expression[curIndex].equals("(")){
                openingBracketCount++;
            }
            else if(expression[curIndex].equals(")")){
                openingBracketCount--;
            }
            curIndex++;
        } while(openingBracketCount!=0 && curIndex < expression.length);
        if(curIndex<expression.length){
            return --curIndex;
        }
        else{
            return null;
        }
    }
}
