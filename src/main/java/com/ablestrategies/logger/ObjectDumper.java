package com.ablestrategies.logger;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * This is for assembling a String that is a man-readable version of and Object, Array, etc.
 */
public class ObjectDumper {

    /** Output is assembled here. */
    private StringBuilder buffer;

    /** How deep to recurse. */
    private int maxDepthRecursion = 3;

    /** Max array/collection/map elements to dump. */
    private int maxElementsPerArray = 5;

    /** true to dump statics, inaccessibles, etc. */
    private boolean showEverything = false;

    /** Keep track of previously-dumped objects, so we don't have infinite recursion. */
    private HashSet<Object> visited;

    /**
     * Ctor.
     * @param maxDepthRecursion How deep to recurse.
     * @param maxElementsPerArray Max array/collection/map elements to dump.
     * @param showEverything true to dump statics, inaccessibles, etc.
     */
    public ObjectDumper(int maxDepthRecursion, int maxElementsPerArray, boolean showEverything) {
        this.maxDepthRecursion = Math.min(maxDepthRecursion, 5);
        this.maxElementsPerArray = Math.min(maxElementsPerArray, 100);
        this.showEverything = showEverything;
    }

    /**
     * Dump an object, array, or whatever.
     * @param object To be dumped.
     * @param name It's variable or field name. (as that cannot be gleaned from the value.)
     * @return String representation of object.
     */
    public String dump(Object object, String name) {
        buffer = new StringBuilder();
        visited = new HashSet<>();
        dumpRecursively(object, name, 0);
        return buffer.toString();
    }

    /**
     * Recursive dump of object, array, or whatever. Append its name, then continue.
     * @param object To be dumped.
     * @param name It's variable or field name. (as that cannot be gleaned from the value.)
     * @param currentDepth recursion depth, add one each time you recurse.
     */
    protected void dumpRecursively(Object object, String name, int currentDepth) {
        if(currentDepth > maxDepthRecursion) {
            return;
        }
        if(name == null) {
            name = "Unknown";
        }
        if(object == null) {
            append(name + " (null)", currentDepth, true);
            return;
        }
        String id = String.format("@%08x", System.identityHashCode(object));
        String title = object.getClass().getSimpleName() + " " + name + "; (" + id + ")";
        append(title, currentDepth, true);
        dumpObjectTypes(object, currentDepth);
    }

    /**
     * Determine an object's type (array, list, etc.) and then dump it.
     * @param object To be dumped.
     * @param currentDepth recursion depth, add one each time you recurse.
     */
    private void dumpObjectTypes(Object object, int currentDepth) {
        if(object.getClass().isPrimitive() || object instanceof String ||
                object instanceof Number || object instanceof Boolean || object instanceof Character) {
            dumpToString(object, currentDepth);
        } else if(object.getClass().isArray()) {
            dumpArray(object, currentDepth);
        } else if(object instanceof Map) {
            dumpMap((Map<?,?>) object, currentDepth);
        } else if(object instanceof Collection) {
            dumpCollection((Collection<?>) object, currentDepth);
        } else if(object instanceof Throwable) {
            dumpThrowable((Throwable) object, currentDepth);
        } else if(object instanceof StackTraceElement) {
            dumpStackTraceElement((StackTraceElement) object, currentDepth);
        } else {
            dumpObject(object, currentDepth);
        }
    }

    /**
     * Dump a String, Number, or other value that has a simple and useful toString() method.
     * @param obj To be dumped.
     * @param currentDepth recursion depth.
     */
    protected void dumpToString(Object obj, int currentDepth) {
        append(" = " + obj.toString(), currentDepth, false);
    }

    /**
     * Dump an array.
     * @param array To be dumped.
     * @param currentDepth recursion depth.
     */
    protected void dumpArray(Object array, int currentDepth) {
        if(alreadyVisited(array, currentDepth)) {
            return;
        }
        append("java.lang.Array [", currentDepth, true);
        int arrayLgt = Array.getLength(array);
        int formatLgt = Math.min(arrayLgt, maxElementsPerArray);
        for (int i = 0; i < formatLgt; i++) {
            dumpRecursively(Array.get(array, i), "[" + i + "]", currentDepth + 1);
        }
        if(arrayLgt > maxElementsPerArray) {
            append("...and " + (arrayLgt - maxElementsPerArray) + " more.", currentDepth + 1, true);
        }
        append("]", currentDepth, true);
    }

    /**
     * Dump a map.
     * @param map To be dumped.
     * @param currentDepth recursion depth.
     */
    protected void dumpMap(Map<?,?> map, int currentDepth) {
        if(alreadyVisited(map, currentDepth)) {
            return;
        }
        append( map.getClass().getName() + " [", currentDepth, true);
        int collectionLength = map.size();
        int index = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            dumpRecursively(entry.getValue(), "[" + entry.getKey() + "|",  currentDepth + 1);
            if(++index >= collectionLength || index >= maxElementsPerArray) {
                break;
            }
        }
        if(collectionLength > maxElementsPerArray) {
            append("...and " + (collectionLength - maxElementsPerArray) + " more.", currentDepth + 1, true);
        }
        append("]", currentDepth, true);
    }

    /**
     * Dump a collection, such as a List.
     * @param collection To be dumped.
     * @param currentDepth recursion depth.
     */
    protected void dumpCollection(Collection<?> collection, int currentDepth) {
        if(alreadyVisited(collection, currentDepth)) {
            return;
        }
        append( collection.getClass().getName() + " [", currentDepth, true);
        int collectionLength = collection.size();
        int index = 0;
        for (Object obj : collection) {
            dumpRecursively(obj, "[" + index + "|",  currentDepth + 1);
            if(++index >= collectionLength || index >= maxElementsPerArray) {
                break;
            }
        }
        if(collectionLength > maxElementsPerArray) {
            append("...and " + (collectionLength - maxElementsPerArray) + " more.", currentDepth + 1, true);
        }
        append("]", currentDepth, true);
    }

    /**
     * Dump a Throwable, such as an Exception.
     * @param throwable To be dumped.
     * @param currentDepth recursion depth.
     */
    protected void dumpThrowable(Throwable throwable, int currentDepth) {
        if(alreadyVisited(throwable, currentDepth)) {
            return;
        }
        String desc = throwable.getClass().getName() + ": " + throwable.getMessage();
        append(" = " + desc, currentDepth, false);
        dumpRecursively(throwable.getStackTrace(), "stackTrace", currentDepth + 1);
        dumpRecursively(throwable.getCause(), "cause", currentDepth + 1);
    }

    /**
     * Dump a StackTraceElement.
     * @param element To be dumped.
     * @param currentDepth recursion depth.
     */
    protected void dumpStackTraceElement(StackTraceElement element, int currentDepth) {
        if(alreadyVisited(element, currentDepth)) {
            return;
        }
        String desc = element.getClassName() + "." + element.getMethodName() + "(" + element.getLineNumber() + ")";
        append(" = " + desc, currentDepth, false);
    }

    /**
     * Dump an Object, other than those that are handled by other dumpers..
     * @param object To be dumped.
     * @param currentDepth recursion depth.
     */
    protected void dumpObject(Object object, int currentDepth) {
        if(alreadyVisited(object, currentDepth)) {
            return;
        }
        Class<?> clazz = object.getClass();
        Field[] fields = getFields(clazz);
        for(Field field : fields) {
            boolean isSynthetic = field.isSynthetic();
            boolean isEnumConstant = field.isEnumConstant();
            boolean isStatic = (field.getModifiers() & Modifier.STATIC) != 0;
            boolean isPublic = (field.getModifiers() & Modifier.PUBLIC) != 0;
            String name = "Unknown";
            if(!(isSynthetic || isEnumConstant || (isStatic && !showEverything))) {
                try {
                    field.setAccessible(true);
                    name = field.getName();
                    if(isStatic) {
                        name = name + " (static)";
                    }
                    if(isPublic) {
                        name = name + " (public)";
                    }
                    dumpRecursively(field.get(object), name, currentDepth + 1);
                } catch (InaccessibleObjectException | IllegalAccessException e) {
                    if(showEverything) {
                        append(name + " (not accessible)", currentDepth + 1, true);
                    }
                }
            }
        }
    }

    /**
     * Get an array of Fields that appear in an Object.
     * @param clazz The object's class.
     * @return The fields to be dumped.
     */
    private Field[] getFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Set<Field> allFields = new HashSet<>(Arrays.stream(declaredFields).toList());
        if(showEverything) {
            Field[] inheritedFields = clazz.getFields();
            allFields.addAll(Arrays.stream(inheritedFields).toList());
        }
        return (Field[])allFields.toArray(new Field[0]);
    }

    /**
     * Has a specific object (or array, etc.) already been dumped?
     * @param object To be dumped.
     * @param currentDepth recursion depth.
     * @return true if it has already been dumped and, if so, an "already dumped" message will be dumped.
     */
    protected boolean alreadyVisited(Object object, int currentDepth) {
        if (visited.contains(object)) {
            append("(already shown above)" , currentDepth, false);
            return true;
        }
        visited.add(object);
        return false;
    }

    /**
     * Dump to the buffer or, if recursion is too deep, discard.
     * @param string To be dumped.
     * @param currentDepth recursion depth.
     * @param onNewLine true to prepend a newline and to indent the string.
     */
    protected void append(String string, int currentDepth, boolean onNewLine) {
        if(currentDepth > maxDepthRecursion) {
            return;
        }
        if(onNewLine && !buffer.isEmpty()) {
            buffer.append(System.lineSeparator());
            String indentFmt = "%s";
            if (currentDepth > 0) {
                indentFmt = "%" + (currentDepth * 2) + "s";
            }
            String indent = String.format(indentFmt, "");
            buffer.append(indent);
        }
        buffer.append(string);
    }

}
