package org.unl.music.base.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.unl.music.base.controller.dao.dao_models.DaoArtista;
import org.unl.music.base.controller.dao.dao_models.DaoCancion;
import org.unl.music.base.controller.data_struct.list.LinkedList;
import org.unl.music.base.models.Artista;
import org.unl.music.base.models.Cancion;
import org.unl.music.base.models.RolArtistaEnum;

import com.github.javaparser.quality.NotNull;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AnonymousAllowed

public class ArtistaService {
    private DaoArtista da;

    public ArtistaService() {
        da = new DaoArtista();
    }

    public void createArtista(@NotEmpty String nombre, @NotEmpty String nacionalidad) throws Exception {
        da.getObj().setNacionidad(nacionalidad);
        da.getObj().setNombres(nombre);
        if (!da.save())
            throw new Exception("No se pudo guardar los datos de artista");
    }

public void aupdateArtista(@NotNull Integer id, @NotEmpty String nombre, @NotEmpty String nacionalidad)
        throws Exception {
    da.setObj(da.listAll().get(id));
    da.getObj().setNacionidad(nacionalidad);
    da.getObj().setNombres(nombre);

    if (!da.update(id))
        throw new Exception("No se pudo modificar los datos de artista");

    // Sincronizar en canciones
    DaoCancion daoCancion = new DaoCancion();
    LinkedList<Cancion> canciones = daoCancion.listAll();
    for (int i = 0; i < canciones.getLength(); i++) {
        Cancion cancion = canciones.get(i);
        if (cancion != null && cancion.getArtista() != null && cancion.getArtista().getId() != null) {
            if (cancion.getArtista().getId().equals(id)) {
                cancion.setArtista(da.getObj());
                daoCancion.setObj(cancion);
                daoCancion.update(i); // Usa `i` si ese es el índice en tu DAO personalizado
            }
        }
    }
}

    public List<Artista> listAll() {
        // System.out.println("**********Entro aqui");
        // System.out.println("lengthy "+Arrays.asList(da.listAll().toArray()).size());
        return (List<Artista>) Arrays.asList(da.listAll().toArray());
    }

    public List<String> listCountry() {
        List<String> nacionalidades = new ArrayList<>();
        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes) {
            Locale locale = new Locale("", countryCode);
            nacionalidades.add(locale.getDisplayCountry());
            // System.out.println("Country Code: " + locale.getCountry() + ", Country Name:
            // " + locale.getDisplayCountry());
        }

        return nacionalidades;
    }

    public List<String> listRolArtista() {
        List<String> lista = new ArrayList<>();
        for (RolArtistaEnum r : RolArtistaEnum.values()) {
            lista.add(r.toString());
        }
        return lista;
    }

    public List<Artista> order(String atributo, Integer type) {
        System.out.println(atributo + "  " + type);
        if (atributo.equalsIgnoreCase("nombres"))
            return (List<Artista>) Arrays.asList(da.orderQ(type).toArray());
        else if (atributo.equalsIgnoreCase("nacionalidad"))
            return (List<Artista>) Arrays.asList(da.orderLocate(type).toArray());
        else
            return (List<Artista>) Arrays.asList(da.listAll().toArray());
    }
}
