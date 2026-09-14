package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.afs.integratedMachine.utils.Meta;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.stream.Collectors;

public class CompartmentPropertyExpression implements CompartmentProperty {
    private final String expression;
    private final Map<String, CompartmentProperty> arguments;
    private ExpressionNode cache;

    public CompartmentPropertyExpression(String expression, Map<String, CompartmentProperty> arguments){
        this.expression = expression;
        this.arguments = arguments;
    }

    public String getExpression() {
        return expression;
    }

    public Map<String, CompartmentProperty> getArguments() {
        return arguments;
    }

    public ExpressionNode getNode(){
        if(cache == null){
            try{
                cache = ExpressionNode.parse(expression);
            }
            catch (Exception e){
                Meta.LOGGER.error("can not parse expression: [{}], an error happened: {}", expression, e);
                cache = new ExpressionNode.NumberNode(0);
            }
        }
        return cache;
    }

    public static MapCodec<CompartmentPropertyExpression> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    Codec.STRING.fieldOf("expression").forGetter(CompartmentPropertyExpression::getExpression),
                    Codec.unboundedMap(Codec.STRING, Codec.lazyInitialized(() -> CompartmentProperty.CODEC))
                            .fieldOf("arguments").forGetter(CompartmentPropertyExpression::getArguments)
            ).apply(inst, CompartmentPropertyExpression::new)
    );

    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        Map<String, Integer> values = arguments.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getPropertyValue(level, blocks)
                ));
        return getNode().getValue(values);
    }

    public static abstract class ExpressionNode{
        public abstract int getValue(Map<String, Integer> values);

        public static ExpressionNode parse(String expression){
            return new Parser(expression).parse();
        }

        private static final class NumberNode extends ExpressionNode{
            private final int value;

            private NumberNode(int value){
                this.value = value;
            }

            @Override
            public int getValue(Map<String, Integer> values){
                return value;
            }
        }

        private static final class IdentifierNode extends ExpressionNode{
            private final String name;

            private IdentifierNode(String name){
                this.name = name;
            }

            @Override
            public int getValue(Map<String, Integer> values){
                Integer value = values.get(name);
                if (value == null){
                    throw new IllegalArgumentException("unknown identifier: " + name);
                }
                return value;
            }
        }

        private static final class OperatorNode extends ExpressionNode{
            private final char operator;
            private final ExpressionNode left;
            private final ExpressionNode right;

            private OperatorNode(char operator, ExpressionNode left, ExpressionNode right){
                this.operator = operator;
                this.left = left;
                this.right = right;
            }

            @Override
            public int getValue(Map<String, Integer> values){
                int leftValue = left.getValue(values);
                int rightValue = right.getValue(values);
                return switch (operator) {
                    case '+' -> leftValue + rightValue;
                    case '-' -> leftValue - rightValue;
                    case '*' -> leftValue * rightValue;
                    case '/' -> leftValue / rightValue;
                    case '%' -> leftValue % rightValue;
                    default -> throw new IllegalArgumentException("unknown operator: " + operator);
                };
            }
        }

        private static final class Parser{
            private final String text;
            private int pos;

            private Parser(String text){
                this.text = text;
            }

            private ExpressionNode parse(){
                ExpressionNode node = parseAdditive();
                skipWhitespace();
                if (pos < text.length()){
                    throw new IllegalArgumentException("unexpected character: " + text.charAt(pos));
                }
                return node;
            }

            private ExpressionNode parseAdditive(){
                ExpressionNode left = parseMultiplicative();
                while (true){
                    skipWhitespace();
                    if (pos < text.length() && (text.charAt(pos) == '+' || text.charAt(pos) == '-')){
                        char operator = text.charAt(pos++);
                        ExpressionNode right = parseMultiplicative();
                        left = new OperatorNode(operator, left, right);
                    } else {
                        return left;
                    }
                }
            }

            private ExpressionNode parseMultiplicative(){
                ExpressionNode left = parseUnary();
                while (true){
                    skipWhitespace();
                    if (pos < text.length() && (text.charAt(pos) == '*' || text.charAt(pos) == '/' || text.charAt(pos) == '%')){
                        char operator = text.charAt(pos++);
                        ExpressionNode right = parseUnary();
                        left = new OperatorNode(operator, left, right);
                    } else {
                        return left;
                    }
                }
            }

            private ExpressionNode parseUnary(){
                skipWhitespace();
                if (pos < text.length() && (text.charAt(pos) == '+' || text.charAt(pos) == '-')){
                    char operator = text.charAt(pos++);
                    ExpressionNode operand = parseUnary();
                    if (operator == '-'){
                        return new OperatorNode('-', new NumberNode(0), operand);
                    }
                    return operand;
                }
                return parsePrimary();
            }

            private ExpressionNode parsePrimary(){
                skipWhitespace();
                if (pos >= text.length()){
                    throw new IllegalArgumentException("unexpected end of expression");
                }
                char c = text.charAt(pos);
                if (c == '('){
                    pos++;
                    ExpressionNode inner = parseAdditive();
                    skipWhitespace();
                    if (pos >= text.length() || text.charAt(pos) != ')'){
                        throw new IllegalArgumentException("missing closing parenthesis");
                    }
                    pos++;
                    return inner;
                }
                if (Character.isDigit(c)){
                    int start = pos;
                    while (pos < text.length() && Character.isDigit(text.charAt(pos))){
                        pos++;
                    }
                    return new NumberNode(Integer.parseInt(text, start, pos, 10));
                }
                if (Character.isLetter(c) || c == '_'){
                    int start = pos;
                    while (pos < text.length() && (Character.isLetterOrDigit(text.charAt(pos)) || text.charAt(pos) == '_')){
                        pos++;
                    }
                    return new IdentifierNode(text.substring(start, pos));
                }
                throw new IllegalArgumentException("unexpected character: " + c);
            }

            private void skipWhitespace(){
                while (pos < text.length() && Character.isWhitespace(text.charAt(pos))){
                    pos++;
                }
            }
        }
    }
}
