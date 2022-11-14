/**
 * Package containing REST controllers.
 * Generally, they only pass the input data on to the 'domain' layer.
 * Some basic parameter validation might be done.
 * A generic exception mapper maps all runtime exceptions to a 500 http status code in the response.
 * Client error codes are handled not by exceptions, but by 'kind of' 'errors' returned with Vavr's Either.left
 */
package eu.adainius.newsfocused.admin.site.back.infrastructure.controller;