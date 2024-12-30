package com.mycompany.conversor_de_monedas;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Conversor_De_Monedas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        double cantidad, resultado;
        String fromCurrency = "", toCurrency = "";
        
        // Definir cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        do {
            // Mostrar el menú de opciones
            System.out.println("*************************");
            System.out.println("Sea bienvenido/a al conversor de moneda");
            System.out.println("1- Dólar => Peso argentino");
            System.out.println("2- Peso argentino => Dólar");
            System.out.println("3- Dólar => Real Brasileño");
            System.out.println("4- Real Brasileño => Dólar");
            System.out.println("5- Dólar => Peso colombiano");
            System.out.println("6- Peso colombiano => Dólar");
            System.out.println("7- Salir");
            System.out.print("Elija una opción válida: ");
            opcion = scanner.nextInt();

            if (opcion >= 1 && opcion <= 6) {
                // Pedir la cantidad a convertir
                System.out.print("Ingrese la cantidad a convertir: ");
                cantidad = scanner.nextDouble();

                // Determinar las monedas según la opción elegida
                switch (opcion) {
                    case 1:
                        fromCurrency = "USD";
                        toCurrency = "ARS";
                        break;
                    case 2:
                        fromCurrency = "ARS";
                        toCurrency = "USD";
                        break;
                    case 3:
                        fromCurrency = "USD";
                        toCurrency = "BRL";
                        break;
                    case 4:
                        fromCurrency = "BRL";
                        toCurrency = "USD";
                        break;
                    case 5:
                        fromCurrency = "USD";
                        toCurrency = "COP";
                        break;
                    case 6:
                        fromCurrency = "COP";
                        toCurrency = "USD";
                        break;
                }

                // Hacer la solicitud HTTP para obtener la tasa de conversión
                resultado = obtenerTasaDeCambio(client, fromCurrency, toCurrency, cantidad);
                if (resultado != -1) {
                    System.out.println("El valor " + cantidad + " " + fromCurrency + " corresponde a " + resultado + " " + toCurrency);
                } else {
                    System.out.println("Error al obtener la tasa de cambio.");
                }
            } else if (opcion != 7) {
                System.out.println("Opción no válida. Por favor, elija una opción válida.");
            }
            System.out.println();  // Línea en blanco para separación
        } while (opcion != 7);  // Salir cuando el usuario elige la opción 7

        System.out.println("Gracias por su Preferencia, vuelva Pronto!.");
        scanner.close();  // Cerrar el escáner al finalizar
    }

    // Método para obtener la tasa de cambio usando HttpClient y analizarla con Gson
    public static double obtenerTasaDeCambio(HttpClient client, String fromCurrency, String toCurrency, double cantidad) {
        try {
            // Reemplaza {API-KEY} con tu propia clave de API
            String apiKey = "f02f27d7547ba493f20160f7";  // Pon aquí tu clave
            // Construir la solicitud HTTP con el par de monedas
            String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + fromCurrency + "/" + toCurrency;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Enviar la solicitud
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Imprimir la respuesta para depuración
            System.out.println("Response: " + response.body());

            // Analizar el JSON usando Gson
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

            if (jsonResponse.get("result").getAsString().equals("success")) {
                // Obtener la tasa de cambio
                double tasaDeCambio = jsonResponse.get("conversion_rate").getAsDouble();
                // Realizar la conversión
                return cantidad * tasaDeCambio;
            } else {
                System.out.println("Error: " + jsonResponse.get("error-type").getAsString());
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;  // Retornar -1 si ocurre algún error
        }
    }
}






