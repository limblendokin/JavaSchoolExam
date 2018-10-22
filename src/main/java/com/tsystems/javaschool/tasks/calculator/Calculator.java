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

    // I could use java.util.Queue, but i didn't know if this permitted
    // Because this task can be done with javax.script.ScriptEngine
    // easily, and not seems like this task was about that.
    private String[] queue;
    private int queueRearPointer = 0;
    private int queueFrontPointer = 0;

    private String queueLastOperationFromLastEvaluation = "+";
    Double result = 0.0;

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
        // Using lookaround and lookbehind to split statement into operands and operations
        expression = statement.split("(?<=[+\\-=*/()])|(?=[+\\-=*/()])");

        // init values of state machine
        // machine queues up elements
        OperationState operationState = OperationState.ADDITION_OR_SUBSTRACTION;
        ExpressionReadState readState = ExpressionReadState.READING_OPERAND;
        queue = new String[expression.length+1];
        Double solvedQueue;
        String prevStateOperation;
        String prevReadedOperand = "0";
        if(expression[0].equals("-")){
            // if first operand is negative, then we start parsing from operation read
            // Also our first operand in queue is "0"
            readState = ExpressionReadState.READING_OPERATION;
        }
        else if(expression.length == 1){
            // if
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
                    // store operand in the var to decide what to do with it in the reading operation section
                    prevReadedOperand = expression[i];
                }
                else if(expression[i].charAt(0) == '('){
                    Calculator c = new Calculator();
                    Integer openingBracketIndex = findFirstOpeningBracket(statement);
                    Integer closingBracketIndex = findClosingBracketStatementIndex(statement, openingBracketIndex);
                    if(closingBracketIndex != null) {
                        prevReadedOperand = c.evaluate(statement.substring(openingBracketIndex+1, closingBracketIndex));
                        if(prevReadedOperand == null){
                            // if evaluation of expression in parentheses fails
                            return null;
                        }
                        i = findExpressionClosingBracketIndex(expression, i);
                    }
                    else{
                        // if there is no closing bracket in the statement
                        return null;
                    }
                }
                // if expression has multiple operators together
                else return null;
                readState = ExpressionReadState.READING_OPERATION;
            }
            else {
                if(didStateChanged(expression[i], operationState)){
                    operationState = changeState(operationState);
                    // if previous operation are multiplication or division and next operation are
                    // addition or subtraction, then we have to add operand from reading state,
                    // evaluate queued elements, queue accumulating value(result) and operation, that was
                    // before this queue
                    if(operationState == OperationState.ADDITION_OR_SUBSTRACTION) {
                        enQueue(prevReadedOperand);
                        enQueue(expression[i]);
                        prevStateOperation = queueLastOperationFromLastEvaluation;
                        solvedQueue = evaluateQueue();
                        if(solvedQueue==null){
                            return null;
                        }
                        enQueue(Double.toString(result));
                        enQueue(prevStateOperation);
                        enQueue(Double.toString(solvedQueue));
                        enQueue(queueLastOperationFromLastEvaluation);
                    }
                    // if opposite, then we evaluate queue first, and then
                    // adding operand from read state
                    else{
                        result = evaluateQueue();
                        if(result == null){
                            return null;
                        }
                        enQueue(prevReadedOperand);
                        enQueue(expression[i]);
                    }
                }
                else{
                    // if current and previous operation are same,
                    // then just add operand from read state and operation in the queue
                    enQueue(prevReadedOperand);
                    enQueue(expression[i]);
                }
                readState = ExpressionReadState.READING_OPERAND;
            }
        }
        if(readState == ExpressionReadState.READING_OPERAND)
            // if expression ends with operator
            return null;
        enQueue(prevReadedOperand);
        if(operationState == OperationState.MULTIPLICATION_OR_DIVISION) {
            prevStateOperation = queueLastOperationFromLastEvaluation;
            enQueue("");
            solvedQueue = evaluateQueue();
            if(solvedQueue == null)
                // zero divide
                return null;
            enQueue(Double.toString(result));
            enQueue(prevStateOperation);
            enQueue(Double.toString(solvedQueue));
        }
        result = evaluateQueue();

        if(result == null){
            // this happens, when there is a zero divide int the queue
            return null;
        }
        // cut the zeroes from the end of string
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

    /**
     *
     * @param operationState Current operation state
     * @return Returns oposite operation state
     */

    private OperationState changeState(OperationState operationState){
        return operationState == OperationState.ADDITION_OR_SUBSTRACTION ? OperationState.MULTIPLICATION_OR_DIVISION : OperationState.ADDITION_OR_SUBSTRACTION;
    }

    /**
     *
     * @return Retrieve value from the queue
     */
    private String deQueue(){
        if(queueRearPointer<queue.length)
            return queue[queueRearPointer++];
        else
            return null;
    }

    /**
     *
     * @param value Store value in the queue
     */
    private void enQueue(String value){
        queue[queueFrontPointer++] = value;
    }

    /**
     *
     * @return Perform queued up operations, return result and store last, not performed operation in a var
     */
    private Double evaluateQueue(){
        Double acc = 0.0;
        Double operand = 0.0;
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
        queueLastOperationFromLastEvaluation = operation;
        // empty queue
        queueRearPointer = 0;
        queueFrontPointer = 0;
        return acc;
    }
    private boolean isQueueEmpty(){
        return queueFrontPointer <= queueRearPointer;
    }
    // I could use regex to determine if it is parsable, but I didn't figure out
    // if I can use new imports, even if they are in JDK_8
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
        --curIndex;
        if(curIndex<statement.length()){
            return curIndex;
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
        --curIndex;
        if(curIndex<expression.length){
            return curIndex;
        }
        else{
            return null;
        }
    }
}
