package com.awk.datastore.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import javax.enterprise.context.ApplicationScoped;
import javax.json.*;
import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class ResourceService {

    private Firestore db;

    public ResourceService() {
        db = FirestoreOptions.getDefaultInstance().getService();
    }

    public Collection<JsonObject> getResources(String collectionId) {
        ApiFuture<QuerySnapshot> future = db.collection(collectionId).get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            throw new NotFoundException(e);
        }

        List<JsonObject> resourceList = new ArrayList<>();
        for (DocumentSnapshot document : documents) {

            JsonObject jsonObject = mapToJSON(Objects.requireNonNull(document.getData()));
            resourceList.add(jsonObject);
        }

        return resourceList;
    }

    public Collection<String> getResourceIds() {
        List<String> collectionIds = new ArrayList<>();
        db.listCollections().forEach(collectionReference -> collectionIds.add(collectionReference.getId()));
        return collectionIds;
    }

    public JsonObject getResource( String collectionId, String resourceId ) throws NotFoundException {
        DocumentReference docRef = db.collection(collectionId).document(resourceId);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NotFoundException(e);
        }
        JsonObject jsonObject;
        if (document.exists()) {
            jsonObject = mapToJSON(Objects.requireNonNull(document.getData()));
        } else {
            throw new NotFoundException("No such document!");
        }

        return jsonObject;
    }

    public void addResource( String resourceMapId, JsonObject resourceJson ) {

        db.collection(resourceMapId).document(resourceJson.getString("id")).set(resourceJson);
    }

    private JsonObject mapToJSON(Map<String, Object> map) {

        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ( isSingleElement(value) ) {
                builder.add(key, getSingleValueAsString(((Map) value)));

            } else if (value instanceof Map) {
                Map<String, Object> subMap = (Map<String, Object>) value;
                builder.add(key, mapToJSON(subMap));
            } else if (value instanceof List) {
                builder.add(key, listToJSONArray((List<Object>) value));
            }
            else {
                builder.add(key, value.toString());
            }
        }
        return builder.build();
    }

    private String getSingleValueAsString(Map value) {
        if( value.get("valueType").equals("STRING") ) {
            return value.get("string").toString();
        }
        return "Not yet implemented for " + value.get("valueType");
    }

    private boolean isSingleElement(Object value) {
        return ( value instanceof Map
                && ((Map) value).containsKey("valueType"));
    }

    private JsonArray listToJSONArray(List<Object> list) {
        JsonArrayBuilder builder = Json.createArrayBuilder();

        for(Object value: list) {
            if ( isSingleElement(value) ) {
                builder.add(getSingleValueAsString(((Map) value)));

            } else if (value instanceof Map) {
                builder.add(mapToJSON((Map<String, Object>) value));
            }
            else if(value instanceof List) {
                builder.add(listToJSONArray((List<Object>) value));
            }
            else {
                builder.add(value.toString());
            }
        }
        return builder.build();
    }
}
