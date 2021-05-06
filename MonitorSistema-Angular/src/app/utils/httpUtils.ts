import { HttpHeaders } from "@angular/common/http";


/**
     * Valores para el header
     */
export const headerVal = {
  'Content-Type': 'application/json',
   'Access-Control-Allow-Headers': 'Content-Type'
};

/**
 * Se crea un json de opciones para la petición
 * la llave observe es necesaria para poder acceder a la respuesta completa, no solo al body
 **/
 export const options = {
  headers: new HttpHeaders(headerVal),
  observe: 'response' as 'response'
};
