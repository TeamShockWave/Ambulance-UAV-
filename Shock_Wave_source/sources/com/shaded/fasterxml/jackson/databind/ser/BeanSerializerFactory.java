package com.shaded.fasterxml.jackson.databind.ser;

import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.BeanDescription;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.MapperFeature;
import com.shaded.fasterxml.jackson.databind.PropertyName;
import com.shaded.fasterxml.jackson.databind.SerializationConfig;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.shaded.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.shaded.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.shaded.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter;
import com.shaded.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.shaded.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
import com.shaded.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.shaded.fasterxml.jackson.databind.type.TypeBindings;
import com.shaded.fasterxml.jackson.databind.util.ArrayBuilders;
import com.shaded.fasterxml.jackson.databind.util.ClassUtil;
import com.shaded.fasterxml.jackson.databind.util.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class BeanSerializerFactory extends BasicSerializerFactory implements Serializable {
    public static final BeanSerializerFactory instance = new BeanSerializerFactory((SerializerFactoryConfig) null);
    private static final long serialVersionUID = 1;

    protected BeanSerializerFactory(SerializerFactoryConfig serializerFactoryConfig) {
        super(serializerFactoryConfig);
    }

    public SerializerFactory withConfig(SerializerFactoryConfig serializerFactoryConfig) {
        if (this._factoryConfig == serializerFactoryConfig) {
            return this;
        }
        if (getClass() == BeanSerializerFactory.class) {
            return new BeanSerializerFactory(serializerFactoryConfig);
        }
        throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
    }

    /* access modifiers changed from: protected */
    public Iterable<Serializers> customSerializers() {
        return this._factoryConfig.serializers();
    }

    public JsonSerializer<Object> createSerializer(SerializerProvider serializerProvider, JavaType javaType) throws JsonMappingException {
        boolean z;
        SerializationConfig config = serializerProvider.getConfig();
        BeanDescription introspect = config.introspect(javaType);
        JsonSerializer<Object> findSerializerFromAnnotation = findSerializerFromAnnotation(serializerProvider, introspect.getClassInfo());
        if (findSerializerFromAnnotation != null) {
            return findSerializerFromAnnotation;
        }
        JavaType modifyTypeByAnnotation = modifyTypeByAnnotation(config, introspect.getClassInfo(), javaType);
        if (modifyTypeByAnnotation == javaType) {
            z = false;
        } else if (modifyTypeByAnnotation.getRawClass() != javaType.getRawClass()) {
            introspect = config.introspect(modifyTypeByAnnotation);
            z = true;
        } else {
            z = true;
        }
        Converter<Object, Object> findSerializationConverter = introspect.findSerializationConverter();
        if (findSerializationConverter == null) {
            return _createSerializer2(serializerProvider, modifyTypeByAnnotation, introspect, z);
        }
        JavaType outputType = findSerializationConverter.getOutputType(serializerProvider.getTypeFactory());
        return new StdDelegatingSerializer(findSerializationConverter, outputType, _createSerializer2(serializerProvider, outputType, introspect, true));
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0065 A[LOOP:1: B:24:0x0065->B:27:0x0075, LOOP_START, PHI: r0 
      PHI: (r0v1 com.shaded.fasterxml.jackson.databind.JsonSerializer) = (r0v0 com.shaded.fasterxml.jackson.databind.JsonSerializer), (r0v4 com.shaded.fasterxml.jackson.databind.JsonSerializer) binds: [B:23:0x005d, B:27:0x0075] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonSerializer<?> _createSerializer2(com.shaded.fasterxml.jackson.databind.SerializerProvider r5, com.shaded.fasterxml.jackson.databind.JavaType r6, com.shaded.fasterxml.jackson.databind.BeanDescription r7, boolean r8) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r4 = this;
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4.findSerializerByAnnotations(r5, r6, r7)
            if (r0 == 0) goto L_0x0007
        L_0x0006:
            return r0
        L_0x0007:
            com.shaded.fasterxml.jackson.databind.SerializationConfig r2 = r5.getConfig()
            boolean r1 = r6.isContainerType()
            if (r1 == 0) goto L_0x005d
            if (r8 != 0) goto L_0x0018
            r0 = 0
            boolean r8 = r4.usesStaticTyping(r2, r7, r0)
        L_0x0018:
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4.buildContainerSerializer(r5, r6, r7, r8)
            if (r0 != 0) goto L_0x0006
        L_0x001e:
            if (r0 != 0) goto L_0x0036
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4.findSerializerByLookup(r6, r2, r7, r8)
            if (r0 != 0) goto L_0x0036
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4.findSerializerByPrimaryType(r5, r6, r7, r8)
            if (r0 != 0) goto L_0x0036
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4.findBeanSerializer(r5, r6, r7)
            if (r0 != 0) goto L_0x0036
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4.findSerializerByAddonType(r2, r6, r7, r8)
        L_0x0036:
            if (r0 == 0) goto L_0x0006
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r4._factoryConfig
            boolean r1 = r1.hasSerializerModifiers()
            if (r1 == 0) goto L_0x0006
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r4._factoryConfig
            java.lang.Iterable r1 = r1.serializerModifiers()
            java.util.Iterator r3 = r1.iterator()
            r1 = r0
        L_0x004b:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x0078
            java.lang.Object r0 = r3.next()
            com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier r0 = (com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier) r0
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.modifySerializer(r2, r7, r1)
            r1 = r0
            goto L_0x004b
        L_0x005d:
            java.lang.Iterable r1 = r4.customSerializers()
            java.util.Iterator r1 = r1.iterator()
        L_0x0065:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x001e
            java.lang.Object r0 = r1.next()
            com.shaded.fasterxml.jackson.databind.ser.Serializers r0 = (com.shaded.fasterxml.jackson.databind.ser.Serializers) r0
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.findSerializer(r2, r6, r7)
            if (r0 == 0) goto L_0x0065
            goto L_0x001e
        L_0x0078:
            r0 = r1
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.BeanSerializerFactory._createSerializer2(com.shaded.fasterxml.jackson.databind.SerializerProvider, com.shaded.fasterxml.jackson.databind.JavaType, com.shaded.fasterxml.jackson.databind.BeanDescription, boolean):com.shaded.fasterxml.jackson.databind.JsonSerializer");
    }

    @Deprecated
    public final JsonSerializer<Object> findBeanSerializer(SerializerProvider serializerProvider, JavaType javaType, BeanDescription beanDescription, BeanProperty beanProperty) throws JsonMappingException {
        return findBeanSerializer(serializerProvider, javaType, beanDescription);
    }

    public JsonSerializer<Object> findBeanSerializer(SerializerProvider serializerProvider, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        if (isPotentialBeanType(javaType.getRawClass()) || javaType.isEnumType()) {
            return constructBeanSerializer(serializerProvider, beanDescription);
        }
        return null;
    }

    @Deprecated
    public final TypeSerializer findPropertyTypeSerializer(JavaType javaType, SerializationConfig serializationConfig, AnnotatedMember annotatedMember, BeanProperty beanProperty) throws JsonMappingException {
        return findPropertyTypeSerializer(javaType, serializationConfig, annotatedMember);
    }

    public TypeSerializer findPropertyTypeSerializer(JavaType javaType, SerializationConfig serializationConfig, AnnotatedMember annotatedMember) throws JsonMappingException {
        AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder<?> findPropertyTypeResolver = annotationIntrospector.findPropertyTypeResolver(serializationConfig, annotatedMember, javaType);
        if (findPropertyTypeResolver == null) {
            return createTypeSerializer(serializationConfig, javaType);
        }
        return findPropertyTypeResolver.buildTypeSerializer(serializationConfig, javaType, serializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, serializationConfig, annotationIntrospector, javaType));
    }

    public TypeSerializer findPropertyContentTypeSerializer(JavaType javaType, SerializationConfig serializationConfig, AnnotatedMember annotatedMember) throws JsonMappingException {
        JavaType contentType = javaType.getContentType();
        AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder<?> findPropertyContentTypeResolver = annotationIntrospector.findPropertyContentTypeResolver(serializationConfig, annotatedMember, javaType);
        if (findPropertyContentTypeResolver == null) {
            return createTypeSerializer(serializationConfig, contentType);
        }
        return findPropertyContentTypeResolver.buildTypeSerializer(serializationConfig, contentType, serializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, serializationConfig, annotationIntrospector, contentType));
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final JsonSerializer<Object> constructBeanSerializer(SerializerProvider serializerProvider, BeanDescription beanDescription, BeanProperty beanProperty) throws JsonMappingException {
        return constructBeanSerializer(serializerProvider, beanDescription);
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<Object> constructBeanSerializer(SerializerProvider serializerProvider, BeanDescription beanDescription) throws JsonMappingException {
        List<BeanPropertyWriter> list;
        List<BeanPropertyWriter> list2;
        BeanSerializerBuilder beanSerializerBuilder;
        if (beanDescription.getBeanClass() == Object.class) {
            return serializerProvider.getUnknownTypeSerializer(Object.class);
        }
        SerializationConfig config = serializerProvider.getConfig();
        BeanSerializerBuilder constructBeanSerializerBuilder = constructBeanSerializerBuilder(beanDescription);
        constructBeanSerializerBuilder.setConfig(config);
        List<BeanPropertyWriter> findBeanProperties = findBeanProperties(serializerProvider, beanDescription, constructBeanSerializerBuilder);
        if (findBeanProperties == null) {
            findBeanProperties = new ArrayList<>();
        }
        if (this._factoryConfig.hasSerializerModifiers()) {
            Iterator<BeanSerializerModifier> it = this._factoryConfig.serializerModifiers().iterator();
            while (true) {
                list = findBeanProperties;
                if (!it.hasNext()) {
                    break;
                }
                findBeanProperties = it.next().changeProperties(config, beanDescription, list);
            }
        } else {
            list = findBeanProperties;
        }
        List<BeanPropertyWriter> filterBeanProperties = filterBeanProperties(config, beanDescription, list);
        if (this._factoryConfig.hasSerializerModifiers()) {
            Iterator<BeanSerializerModifier> it2 = this._factoryConfig.serializerModifiers().iterator();
            while (true) {
                list2 = filterBeanProperties;
                if (!it2.hasNext()) {
                    break;
                }
                filterBeanProperties = it2.next().orderProperties(config, beanDescription, list2);
            }
        } else {
            list2 = filterBeanProperties;
        }
        constructBeanSerializerBuilder.setObjectIdWriter(constructObjectIdHandler(serializerProvider, beanDescription, list2));
        constructBeanSerializerBuilder.setProperties(list2);
        constructBeanSerializerBuilder.setFilterId(findFilterId(config, beanDescription));
        AnnotatedMember findAnyGetter = beanDescription.findAnyGetter();
        if (findAnyGetter != null) {
            if (config.canOverrideAccessModifiers()) {
                findAnyGetter.fixAccess();
            }
            JavaType type = findAnyGetter.getType(beanDescription.bindingsForBeanType());
            boolean isEnabled = config.isEnabled(MapperFeature.USE_STATIC_TYPING);
            JavaType contentType = type.getContentType();
            constructBeanSerializerBuilder.setAnyGetter(new AnyGetterWriter(new BeanProperty.Std(findAnyGetter.getName(), contentType, (PropertyName) null, beanDescription.getClassAnnotations(), findAnyGetter, false), findAnyGetter, MapSerializer.construct((String[]) null, type, isEnabled, createTypeSerializer(config, contentType), (JsonSerializer<Object>) null, (JsonSerializer<Object>) null)));
        }
        processViews(config, constructBeanSerializerBuilder);
        if (this._factoryConfig.hasSerializerModifiers()) {
            beanSerializerBuilder = constructBeanSerializerBuilder;
            for (BeanSerializerModifier updateBuilder : this._factoryConfig.serializerModifiers()) {
                beanSerializerBuilder = updateBuilder.updateBuilder(config, beanDescription, beanSerializerBuilder);
            }
        } else {
            beanSerializerBuilder = constructBeanSerializerBuilder;
        }
        JsonSerializer<?> build = beanSerializerBuilder.build();
        if (build != null || !beanDescription.hasKnownClassAnnotations()) {
            return build;
        }
        return beanSerializerBuilder.createDummy();
    }

    /* access modifiers changed from: protected */
    public ObjectIdWriter constructObjectIdHandler(SerializerProvider serializerProvider, BeanDescription beanDescription, List<BeanPropertyWriter> list) throws JsonMappingException {
        ObjectIdInfo objectIdInfo = beanDescription.getObjectIdInfo();
        if (objectIdInfo == null) {
            return null;
        }
        Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        if (generatorType == ObjectIdGenerators.PropertyGenerator.class) {
            String propertyName = objectIdInfo.getPropertyName();
            int size = list.size();
            for (int i = 0; i != size; i++) {
                BeanPropertyWriter beanPropertyWriter = list.get(i);
                if (propertyName.equals(beanPropertyWriter.getName())) {
                    if (i > 0) {
                        list.remove(i);
                        list.add(0, beanPropertyWriter);
                    }
                    return ObjectIdWriter.construct(beanPropertyWriter.getType(), (String) null, new PropertyBasedObjectIdGenerator(objectIdInfo, beanPropertyWriter), objectIdInfo.getAlwaysAsId());
                }
            }
            throw new IllegalArgumentException("Invalid Object Id definition for " + beanDescription.getBeanClass().getName() + ": can not find property with name '" + propertyName + "'");
        }
        return ObjectIdWriter.construct(serializerProvider.getTypeFactory().findTypeParameters(serializerProvider.constructType(generatorType), (Class<?>) ObjectIdGenerator.class)[0], objectIdInfo.getPropertyName(), serializerProvider.objectIdGeneratorInstance(beanDescription.getClassInfo(), objectIdInfo), objectIdInfo.getAlwaysAsId());
    }

    /* access modifiers changed from: protected */
    public BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter beanPropertyWriter, Class<?>[] clsArr) {
        return FilteredBeanPropertyWriter.constructViewBased(beanPropertyWriter, clsArr);
    }

    /* access modifiers changed from: protected */
    public PropertyBuilder constructPropertyBuilder(SerializationConfig serializationConfig, BeanDescription beanDescription) {
        return new PropertyBuilder(serializationConfig, beanDescription);
    }

    /* access modifiers changed from: protected */
    public BeanSerializerBuilder constructBeanSerializerBuilder(BeanDescription beanDescription) {
        return new BeanSerializerBuilder(beanDescription);
    }

    /* access modifiers changed from: protected */
    public Object findFilterId(SerializationConfig serializationConfig, BeanDescription beanDescription) {
        return serializationConfig.getAnnotationIntrospector().findFilterId(beanDescription.getClassInfo());
    }

    /* access modifiers changed from: protected */
    public boolean isPotentialBeanType(Class<?> cls) {
        return ClassUtil.canBeABeanType(cls) == null && !ClassUtil.isProxyType(cls);
    }

    /* access modifiers changed from: protected */
    public List<BeanPropertyWriter> findBeanProperties(SerializerProvider serializerProvider, BeanDescription beanDescription, BeanSerializerBuilder beanSerializerBuilder) throws JsonMappingException {
        List<BeanPropertyDefinition> findProperties = beanDescription.findProperties();
        SerializationConfig config = serializerProvider.getConfig();
        removeIgnorableTypes(config, beanDescription, findProperties);
        if (config.isEnabled(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)) {
            removeSetterlessGetters(config, beanDescription, findProperties);
        }
        if (findProperties.isEmpty()) {
            return null;
        }
        boolean usesStaticTyping = usesStaticTyping(config, beanDescription, (TypeSerializer) null);
        PropertyBuilder constructPropertyBuilder = constructPropertyBuilder(config, beanDescription);
        ArrayList arrayList = new ArrayList(findProperties.size());
        TypeBindings bindingsForBeanType = beanDescription.bindingsForBeanType();
        for (BeanPropertyDefinition next : findProperties) {
            AnnotatedMember accessor = next.getAccessor();
            if (!next.isTypeId()) {
                AnnotationIntrospector.ReferenceProperty findReferenceType = next.findReferenceType();
                if (findReferenceType == null || !findReferenceType.isBackReference()) {
                    if (accessor instanceof AnnotatedMethod) {
                        arrayList.add(_constructWriter(serializerProvider, next, bindingsForBeanType, constructPropertyBuilder, usesStaticTyping, (AnnotatedMethod) accessor));
                    } else {
                        arrayList.add(_constructWriter(serializerProvider, next, bindingsForBeanType, constructPropertyBuilder, usesStaticTyping, (AnnotatedField) accessor));
                    }
                }
            } else if (accessor != null) {
                if (config.canOverrideAccessModifiers()) {
                    accessor.fixAccess();
                }
                beanSerializerBuilder.setTypeId(accessor);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public List<BeanPropertyWriter> filterBeanProperties(SerializationConfig serializationConfig, BeanDescription beanDescription, List<BeanPropertyWriter> list) {
        String[] findPropertiesToIgnore = serializationConfig.getAnnotationIntrospector().findPropertiesToIgnore(beanDescription.getClassInfo());
        if (findPropertiesToIgnore != null && findPropertiesToIgnore.length > 0) {
            HashSet arrayToSet = ArrayBuilders.arrayToSet(findPropertiesToIgnore);
            Iterator<BeanPropertyWriter> it = list.iterator();
            while (it.hasNext()) {
                if (arrayToSet.contains(it.next().getName())) {
                    it.remove();
                }
            }
        }
        return list;
    }

    /* access modifiers changed from: protected */
    public void processViews(SerializationConfig serializationConfig, BeanSerializerBuilder beanSerializerBuilder) {
        List<BeanPropertyWriter> properties = beanSerializerBuilder.getProperties();
        boolean isEnabled = serializationConfig.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
        int size = properties.size();
        BeanPropertyWriter[] beanPropertyWriterArr = new BeanPropertyWriter[size];
        int i = 0;
        int i2 = 0;
        while (i < size) {
            BeanPropertyWriter beanPropertyWriter = properties.get(i);
            Class[] views = beanPropertyWriter.getViews();
            if (views != null) {
                i2++;
                beanPropertyWriterArr[i] = constructFilteredBeanWriter(beanPropertyWriter, views);
            } else if (isEnabled) {
                beanPropertyWriterArr[i] = beanPropertyWriter;
            }
            i++;
            i2 = i2;
        }
        if (!isEnabled || i2 != 0) {
            beanSerializerBuilder.setFilteredProperties(beanPropertyWriterArr);
        }
    }

    /* access modifiers changed from: protected */
    public void removeIgnorableTypes(SerializationConfig serializationConfig, BeanDescription beanDescription, List<BeanPropertyDefinition> list) {
        AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        HashMap hashMap = new HashMap();
        Iterator<BeanPropertyDefinition> it = list.iterator();
        while (it.hasNext()) {
            AnnotatedMember accessor = it.next().getAccessor();
            if (accessor == null) {
                it.remove();
            } else {
                Class<?> rawType = accessor.getRawType();
                Boolean bool = (Boolean) hashMap.get(rawType);
                if (bool == null) {
                    bool = annotationIntrospector.isIgnorableType(serializationConfig.introspectClassAnnotations(rawType).getClassInfo());
                    if (bool == null) {
                        bool = Boolean.FALSE;
                    }
                    hashMap.put(rawType, bool);
                }
                if (bool.booleanValue()) {
                    it.remove();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeSetterlessGetters(SerializationConfig serializationConfig, BeanDescription beanDescription, List<BeanPropertyDefinition> list) {
        Iterator<BeanPropertyDefinition> it = list.iterator();
        while (it.hasNext()) {
            BeanPropertyDefinition next = it.next();
            if (!next.couldDeserialize() && !next.isExplicitlyIncluded()) {
                it.remove();
            }
        }
    }

    /* access modifiers changed from: protected */
    public BeanPropertyWriter _constructWriter(SerializerProvider serializerProvider, BeanPropertyDefinition beanPropertyDefinition, TypeBindings typeBindings, PropertyBuilder propertyBuilder, boolean z, AnnotatedMember annotatedMember) throws JsonMappingException {
        String name = beanPropertyDefinition.getName();
        if (serializerProvider.canOverrideAccessModifiers()) {
            annotatedMember.fixAccess();
        }
        JavaType type = annotatedMember.getType(typeBindings);
        BeanProperty.Std std = new BeanProperty.Std(name, type, beanPropertyDefinition.getWrapperName(), propertyBuilder.getClassAnnotations(), annotatedMember, beanPropertyDefinition.isRequired());
        JsonSerializer findSerializerFromAnnotation = findSerializerFromAnnotation(serializerProvider, annotatedMember);
        if (findSerializerFromAnnotation instanceof ResolvableSerializer) {
            ((ResolvableSerializer) findSerializerFromAnnotation).resolve(serializerProvider);
        }
        if (findSerializerFromAnnotation instanceof ContextualSerializer) {
            findSerializerFromAnnotation = ((ContextualSerializer) findSerializerFromAnnotation).createContextual(serializerProvider, std);
        }
        TypeSerializer typeSerializer = null;
        if (ClassUtil.isCollectionMapOrArray(type.getRawClass())) {
            typeSerializer = findPropertyContentTypeSerializer(type, serializerProvider.getConfig(), annotatedMember);
        }
        return propertyBuilder.buildWriter(beanPropertyDefinition, type, findSerializerFromAnnotation, findPropertyTypeSerializer(type, serializerProvider.getConfig(), annotatedMember), typeSerializer, annotatedMember, z);
    }
}
