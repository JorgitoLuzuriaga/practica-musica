package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Album;
import org.unl.music.base.models.Artista_Banda;
import org.unl.music.base.models.Cancion;
import org.unl.music.base.models.Genero;

import java.util.HashMap;

import org.unl.music.base.controller.Utiles;
import org.unl.music.base.controller.dao.AdapterDao;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoCancion extends AdapterDao<Cancion> {
    private Cancion obj;

    public DaoCancion() {
        super(Cancion.class);
        // TODO Auto-generated constructor stub
    }

    public Cancion getObj() {
        if (obj == null)
            this.obj = new Cancion();
        return this.obj;
    }

    public void setObj(Cancion obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength()+1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    


    public LinkedList<HashMap<String, String>> all() throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!this.listAll().isEmpty()) {
            Cancion[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

public HashMap<String, String> toDict(Cancion c) throws Exception {
    DaoGenero daoGenero = new DaoGenero();
    DaoAlbum  daoAlbum  = new DaoAlbum();

    HashMap<String, String> map = new HashMap<>();
    map.put("id",      c.getId().toString());
    map.put("nombre",  c.getNombre());
    map.put("genero",  daoGenero.findById(c.getId_genero()).getNombre());
    map.put("albun",   daoAlbum.findById(c.getId_album()).getNombre());
    map.put("url",     c.getUrl());
    map.put("tipo",    c.getTipo().toString());
    return map;
}

public LinkedList<HashMap<String, String>> search(String attribute, String text, Integer type) throws Exception {
    LinkedList<HashMap<String, String>> lista = all();
    LinkedList<HashMap<String, String>> resp = new LinkedList<>();

    if (!lista.isEmpty()) {
        HashMap<String, String>[] arr = lista.toArray();
        System.out.println(attribute + " " + text + " ** *** * * ** * * * *");

        for (HashMap<String, String> m : arr) {
            String valor = m.get(attribute);  // <- puede ser null

            if (valor == null) {
                System.out.println("Advertencia: no se encontró el atributo '" + attribute + "' en el mapa");
                continue; // salta este elemento si no tiene el campo buscado
            }

            valor = valor.toLowerCase();
            String comparado = text.toLowerCase();

            switch (type) {
                case 1:
                    System.out.println(attribute + " " + text + " UNO");
                    if (valor.startsWith(comparado)) {
                        resp.add(m);
                    }
                    break;
                case 2:
                    System.out.println(attribute + " " + text + " DOS");
                    if (valor.endsWith(comparado)) {
                        resp.add(m);
                    }
                    break;
                default:
                    System.out.println(attribute + " " + text + " TRES");
                    if (valor.contains(comparado)) {
                        resp.add(m);
                    }
                    break;
            }
        }
    }

    return resp;
}


//order
    public LinkedList<Cancion> orderLastName(Integer type) {
        LinkedList<Cancion> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            Cancion arr[] = listAll().toArray();
            int n = arr.length;
            if (type == Utiles.ASCEDENTE) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j].getNombre().toLowerCase()
                                .compareTo(arr[min_idx].getNombre().toLowerCase()) < 0) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    Cancion temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j].getNombre().toLowerCase()
                                .compareTo(arr[min_idx].getNombre().toLowerCase()) > 0) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    Cancion temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }

            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(arr);
        }
        return lista;
    }

    private int partition(Cancion arr[], int begin, int end, Integer type) {
        Cancion pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].getNombre().toLowerCase().compareTo(pivot.getNombre().toLowerCase()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    Cancion swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].getNombre().toLowerCase().compareTo(pivot.getNombre().toLowerCase()) > 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    Cancion swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        Cancion swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    private void quickSort(Cancion arr[], int begin, int end, Integer type) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type);

            quickSort(arr, begin, partitionIndex - 1, type);
            quickSort(arr, partitionIndex + 1, end, type);
        }
    }

    public LinkedList<Cancion> orderQ(Integer type) {
        LinkedList<Cancion> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {

            Cancion arr[] = listAll().toArray();
            quickSort(arr, 0, arr.length - 1, type);
            lista.toList(arr);
        }
        return lista;
    }


}
