package io.eagle.aop;


import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
public class CustomSpringELParser {

    public static Object getMarketValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals("marketId")) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        return parser.parseExpression(key).getValue(context, Object.class);
    }

}
