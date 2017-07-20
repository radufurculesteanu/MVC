package ro.teamnet.zth.web;

import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.appl.controller.DepartmentController;
import ro.teamnet.zth.appl.controller.EmployeeController;
import ro.teamnet.zth.fmk.AnnotationScanUtils;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Radu.Furculesteanu on 7/20/2017.
 */
public class MyDispatcherServlet extends HttpServlet{
    Map<String, MethodAttributes> allowedMethods = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try
        {
            Iterable<Class> classIterable = AnnotationScanUtils.getClasses
                    ("ro.teamnet.zth.appl.controller");
            for (Class aClass : classIterable) {
                MyController myController = (MyController)aClass.getAnnotation(MyController.class);
                Method[] methods = aClass.getDeclaredMethods();
                for (Method method : methods) {
                    MethodAttributes methodAttributes = new MethodAttributes();
                    methodAttributes.setMethodName(method.getName());
                    methodAttributes.setMethodType(method.getAnnotation(MyRequestMethod.class).methodType());
                    methodAttributes.setControllerClass(aClass.getName());
                    String key = myController.urlPath() +
                            method.getAnnotation(MyRequestMethod.class).urlPath() + "_" +
                            method.getAnnotation(MyRequestMethod.class).methodType();
                    allowedMethods.put(key,methodAttributes);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodType = "GET";

        dispatchReply(req, resp, methodType);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodType = "POST";
        dispatchReply(req,resp,methodType);
    }

    private void dispatchReply(HttpServletRequest request, HttpServletResponse response, String methodType)
    {
        try
        {
            Object result = dispatch(request,methodType);
            reply(response, result);
        }
        catch(Exception e)
        {
            sendExceptionError(e);
        }
    }

    private Object dispatch(HttpServletRequest request, String methodType)
    {
        StringBuffer url = request.getRequestURL();
        String result = "Test";
        if(url.toString().contains("employees"))
        {
            if(url.toString().contains("all"))
            {
                EmployeeController ec = new EmployeeController();
                result = ec.getAllEmployees();
            }
            if(url.toString().contains("one"))
            {
                EmployeeController ec = new EmployeeController();
                result = ec.getOneEmployee();
            }
        }
        else if(url.toString().contains("departments"))
        {
            if(url.toString().contains("all"))
            {
                DepartmentController dc = new DepartmentController();
                result = dc.getAllDepartments();
            }
        }
        return result;
    }

    public void reply(HttpServletResponse response, Object result)
    {
        try
        {
            response.getWriter().write((String)result);
        }
        catch(IOException ioe)
        {
            sendExceptionError(ioe);
        }
    }

    public void sendExceptionError(Exception e)
    {
        System.out.println(e.toString());
    }



}
