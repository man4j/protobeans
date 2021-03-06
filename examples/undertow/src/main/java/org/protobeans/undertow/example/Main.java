package org.protobeans.undertow.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.protobeans.core.EntryPoint;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.protobeans.undertow.annotation.Initializer;

@EnableUndertow(initializers = @Initializer(initializer = MyServletContainerInitializer.class), resourcesPath = "static")
public class Main {
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}

class MyServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        ctx.addServlet("simple", SimpleServlet.class).addMapping("/simple");
    }
}

class SimpleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        System.out.println(req.getParameter("name"));
        System.out.println(req.getParameter("email"));
        System.out.println(req.getParameter("phone"));
        System.out.println(req.getParameter("message"));
        
        resp.sendRedirect("?successAlert=success!");
    }
}