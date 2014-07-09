package com.github.kazuki43zoo.web.mvc.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.terasoluna.gfw.web.mvc.support.CompositeRequestDataValueProcessor;

public class CustomCompositeRequestDataValueProcessor extends CompositeRequestDataValueProcessor
        implements RequestDataValueProcessor {

    private interface ProcessActionInvoker {
        String invoke(HttpServletRequest request, String action, String httpMethod);
    }

    private final List<ProcessActionInvoker> processActionInvokers;

    public CustomCompositeRequestDataValueProcessor(RequestDataValueProcessor... processors) {
        super(processors);
        List<ProcessActionInvoker> processActionInvokers = new ArrayList<>();
        for (final RequestDataValueProcessor processor : processors) {
            ProcessActionInvoker processActionInvoker;
            try {
                final Method processActionMethodOfSpring3 = processor.getClass().getMethod(
                        "processAction", HttpServletRequest.class, String.class);
                processActionInvoker = new ProcessActionInvoker() {
                    @Override
                    public String invoke(HttpServletRequest request, String action,
                            String httpMethod) {
                        try {
                            return (String) processActionMethodOfSpring3.invoke(processor, request,
                                    action);
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException(e);
                        } catch (InvocationTargetException e) {
                            throw new IllegalStateException(e.getTargetException());
                        }
                    }
                };
            } catch (NoSuchMethodException e) {
                processActionInvoker = new ProcessActionInvoker() {
                    @Override
                    public String invoke(HttpServletRequest request, String action,
                            String httpMethod) {
                        return processor.processAction(request, action, httpMethod);
                    }
                };
            }
            processActionInvokers.add(processActionInvoker);
        }
        this.processActionInvokers = Collections.unmodifiableList(processActionInvokers);
    }

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {

        String result = action;
        for (ProcessActionInvoker invoker : processActionInvokers) {
            result = invoker.invoke(request, action, httpMethod);
            if (!action.equals(result)) {
                break;
            }
        }

        return result;
    }

}
