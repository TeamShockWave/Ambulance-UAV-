package com.shaded.fasterxml.jackson.databind.node;

import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.core.JsonProcessingException;
import com.shaded.fasterxml.jackson.core.JsonToken;
import com.shaded.fasterxml.jackson.databind.JsonNode;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ObjectNode extends ContainerNode<ObjectNode> {
    private final Map<String, JsonNode> _children = new LinkedHashMap();

    public ObjectNode(JsonNodeFactory jsonNodeFactory) {
        super(jsonNodeFactory);
    }

    public ObjectNode deepCopy() {
        ObjectNode objectNode = new ObjectNode(this._nodeFactory);
        for (Map.Entry next : this._children.entrySet()) {
            objectNode._children.put(next.getKey(), ((JsonNode) next.getValue()).deepCopy());
        }
        return objectNode;
    }

    public JsonNodeType getNodeType() {
        return JsonNodeType.OBJECT;
    }

    public JsonToken asToken() {
        return JsonToken.START_OBJECT;
    }

    public int size() {
        return this._children.size();
    }

    public Iterator<JsonNode> elements() {
        return this._children.values().iterator();
    }

    public JsonNode get(int i) {
        return null;
    }

    public JsonNode get(String str) {
        return this._children.get(str);
    }

    public Iterator<String> fieldNames() {
        return this._children.keySet().iterator();
    }

    public JsonNode path(int i) {
        return MissingNode.getInstance();
    }

    public JsonNode path(String str) {
        JsonNode jsonNode = this._children.get(str);
        return jsonNode != null ? jsonNode : MissingNode.getInstance();
    }

    public Iterator<Map.Entry<String, JsonNode>> fields() {
        return this._children.entrySet().iterator();
    }

    public ObjectNode with(String str) {
        JsonNode jsonNode = this._children.get(str);
        if (jsonNode == null) {
            ObjectNode objectNode = objectNode();
            this._children.put(str, objectNode);
            return objectNode;
        } else if (jsonNode instanceof ObjectNode) {
            return (ObjectNode) jsonNode;
        } else {
            throw new UnsupportedOperationException("Property '" + str + "' has value that is not of type ObjectNode (but " + jsonNode.getClass().getName() + ")");
        }
    }

    public ArrayNode withArray(String str) {
        JsonNode jsonNode = this._children.get(str);
        if (jsonNode == null) {
            ArrayNode arrayNode = arrayNode();
            this._children.put(str, arrayNode);
            return arrayNode;
        } else if (jsonNode instanceof ArrayNode) {
            return (ArrayNode) jsonNode;
        } else {
            throw new UnsupportedOperationException("Property '" + str + "' has value that is not of type ArrayNode (but " + jsonNode.getClass().getName() + ")");
        }
    }

    public JsonNode findValue(String str) {
        for (Map.Entry next : this._children.entrySet()) {
            if (str.equals(next.getKey())) {
                return (JsonNode) next.getValue();
            }
            JsonNode findValue = ((JsonNode) next.getValue()).findValue(str);
            if (findValue != null) {
                return findValue;
            }
        }
        return null;
    }

    public List<JsonNode> findValues(String str, List<JsonNode> list) {
        List<JsonNode> list2 = list;
        for (Map.Entry next : this._children.entrySet()) {
            if (str.equals(next.getKey())) {
                if (list2 == null) {
                    list2 = new ArrayList<>();
                }
                list2.add(next.getValue());
            } else {
                list2 = ((JsonNode) next.getValue()).findValues(str, list2);
            }
        }
        return list2;
    }

    public List<String> findValuesAsText(String str, List<String> list) {
        List<String> list2 = list;
        for (Map.Entry next : this._children.entrySet()) {
            if (str.equals(next.getKey())) {
                if (list2 == null) {
                    list2 = new ArrayList<>();
                }
                list2.add(((JsonNode) next.getValue()).asText());
            } else {
                list2 = ((JsonNode) next.getValue()).findValuesAsText(str, list2);
            }
        }
        return list2;
    }

    public ObjectNode findParent(String str) {
        for (Map.Entry next : this._children.entrySet()) {
            if (str.equals(next.getKey())) {
                return this;
            }
            JsonNode findParent = ((JsonNode) next.getValue()).findParent(str);
            if (findParent != null) {
                return (ObjectNode) findParent;
            }
        }
        return null;
    }

    public List<JsonNode> findParents(String str, List<JsonNode> list) {
        List<JsonNode> findParents;
        List<JsonNode> list2 = list;
        for (Map.Entry next : this._children.entrySet()) {
            if (str.equals(next.getKey())) {
                if (list2 == null) {
                    findParents = new ArrayList<>();
                } else {
                    findParents = list2;
                }
                findParents.add(this);
            } else {
                findParents = ((JsonNode) next.getValue()).findParents(str, list2);
            }
            list2 = findParents;
        }
        return list2;
    }

    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        for (Map.Entry next : this._children.entrySet()) {
            jsonGenerator.writeFieldName((String) next.getKey());
            ((BaseJsonNode) next.getValue()).serialize(jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndObject();
    }

    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        typeSerializer.writeTypePrefixForObject(this, jsonGenerator);
        for (Map.Entry next : this._children.entrySet()) {
            jsonGenerator.writeFieldName((String) next.getKey());
            ((BaseJsonNode) next.getValue()).serialize(jsonGenerator, serializerProvider);
        }
        typeSerializer.writeTypeSuffixForObject(this, jsonGenerator);
    }

    public JsonNode set(String str, JsonNode jsonNode) {
        if (jsonNode == null) {
            jsonNode = nullNode();
        }
        this._children.put(str, jsonNode);
        return this;
    }

    public JsonNode setAll(Map<String, JsonNode> map) {
        for (Map.Entry next : map.entrySet()) {
            Object obj = (JsonNode) next.getValue();
            if (obj == null) {
                obj = nullNode();
            }
            this._children.put(next.getKey(), obj);
        }
        return this;
    }

    public JsonNode setAll(ObjectNode objectNode) {
        this._children.putAll(objectNode._children);
        return this;
    }

    public JsonNode replace(String str, JsonNode jsonNode) {
        if (jsonNode == null) {
            jsonNode = nullNode();
        }
        return this._children.put(str, jsonNode);
    }

    public JsonNode without(String str) {
        this._children.remove(str);
        return this;
    }

    public ObjectNode without(Collection<String> collection) {
        this._children.keySet().removeAll(collection);
        return this;
    }

    public JsonNode put(String str, JsonNode jsonNode) {
        if (jsonNode == null) {
            jsonNode = nullNode();
        }
        return this._children.put(str, jsonNode);
    }

    public JsonNode remove(String str) {
        return this._children.remove(str);
    }

    public ObjectNode remove(Collection<String> collection) {
        this._children.keySet().removeAll(collection);
        return this;
    }

    public ObjectNode removeAll() {
        this._children.clear();
        return this;
    }

    public JsonNode putAll(Map<String, JsonNode> map) {
        return setAll(map);
    }

    public JsonNode putAll(ObjectNode objectNode) {
        return setAll(objectNode);
    }

    public ObjectNode retain(Collection<String> collection) {
        this._children.keySet().retainAll(collection);
        return this;
    }

    public ObjectNode retain(String... strArr) {
        return retain((Collection<String>) Arrays.asList(strArr));
    }

    public ArrayNode putArray(String str) {
        ArrayNode arrayNode = arrayNode();
        this._children.put(str, arrayNode);
        return arrayNode;
    }

    public ObjectNode putObject(String str) {
        ObjectNode objectNode = objectNode();
        this._children.put(str, objectNode);
        return objectNode;
    }

    public ObjectNode putPOJO(String str, Object obj) {
        this._children.put(str, pojoNode(obj));
        return this;
    }

    public ObjectNode putNull(String str) {
        this._children.put(str, nullNode());
        return this;
    }

    public ObjectNode put(String str, short s) {
        this._children.put(str, numberNode(s));
        return this;
    }

    public ObjectNode put(String str, Short sh) {
        if (sh == null) {
            this._children.put(str, nullNode());
        } else {
            this._children.put(str, numberNode(sh.shortValue()));
        }
        return this;
    }

    public ObjectNode put(String str, int i) {
        this._children.put(str, numberNode(i));
        return this;
    }

    public ObjectNode put(String str, Integer num) {
        if (num == null) {
            this._children.put(str, nullNode());
        } else {
            this._children.put(str, numberNode(num.intValue()));
        }
        return this;
    }

    public ObjectNode put(String str, long j) {
        this._children.put(str, numberNode(j));
        return this;
    }

    public ObjectNode put(String str, Long l) {
        if (l == null) {
            this._children.put(str, nullNode());
        } else {
            this._children.put(str, numberNode(l.longValue()));
        }
        return this;
    }

    public ObjectNode put(String str, float f) {
        this._children.put(str, numberNode(f));
        return this;
    }

    public ObjectNode put(String str, Float f) {
        if (f == null) {
            this._children.put(str, nullNode());
        } else {
            this._children.put(str, numberNode(f.floatValue()));
        }
        return this;
    }

    public ObjectNode put(String str, double d) {
        this._children.put(str, numberNode(d));
        return this;
    }

    public ObjectNode put(String str, Double d) {
        if (d == null) {
            this._children.put(str, nullNode());
        } else {
            this._children.put(str, numberNode(d.doubleValue()));
        }
        return this;
    }

    public ObjectNode put(String str, BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            putNull(str);
        } else {
            this._children.put(str, numberNode(bigDecimal));
        }
        return this;
    }

    public ObjectNode put(String str, String str2) {
        if (str2 == null) {
            putNull(str);
        } else {
            this._children.put(str, textNode(str2));
        }
        return this;
    }

    public ObjectNode put(String str, boolean z) {
        this._children.put(str, booleanNode(z));
        return this;
    }

    public ObjectNode put(String str, Boolean bool) {
        if (bool == null) {
            this._children.put(str, nullNode());
        } else {
            this._children.put(str, booleanNode(bool.booleanValue()));
        }
        return this;
    }

    public ObjectNode put(String str, byte[] bArr) {
        if (bArr == null) {
            this._children.put(str, nullNode());
        } else {
            this._children.put(str, binaryNode(bArr));
        }
        return this;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this._children.equals(((ObjectNode) obj)._children);
    }

    public int hashCode() {
        return this._children.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder((size() << 4) + 32);
        sb.append("{");
        int i = 0;
        for (Map.Entry next : this._children.entrySet()) {
            if (i > 0) {
                sb.append(",");
            }
            TextNode.appendQuoted(sb, (String) next.getKey());
            sb.append(':');
            sb.append(((JsonNode) next.getValue()).toString());
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
}
