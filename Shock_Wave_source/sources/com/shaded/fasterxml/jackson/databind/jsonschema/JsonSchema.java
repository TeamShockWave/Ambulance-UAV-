package com.shaded.fasterxml.jackson.databind.jsonschema;

import com.shaded.fasterxml.jackson.annotation.JsonCreator;
import com.shaded.fasterxml.jackson.annotation.JsonValue;
import com.shaded.fasterxml.jackson.databind.JsonNode;
import com.shaded.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.shaded.fasterxml.jackson.databind.node.ObjectNode;

@Deprecated
public class JsonSchema {
    private final ObjectNode schema;

    @JsonCreator
    public JsonSchema(ObjectNode objectNode) {
        this.schema = objectNode;
    }

    @JsonValue
    public ObjectNode getSchemaNode() {
        return this.schema;
    }

    public String toString() {
        return this.schema.toString();
    }

    public int hashCode() {
        return this.schema.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof JsonSchema)) {
            return false;
        }
        JsonSchema jsonSchema = (JsonSchema) obj;
        if (this.schema != null) {
            return this.schema.equals(jsonSchema.schema);
        }
        if (jsonSchema.schema != null) {
            return false;
        }
        return true;
    }

    public static JsonNode getDefaultSchemaNode() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("type", "any");
        return objectNode;
    }
}
