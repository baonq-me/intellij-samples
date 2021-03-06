package com.jetbrains.analysis;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"StatementWithEmptyBody", "unused", "Java9CollectionFactory", "MismatchedQueryAndUpdateOfCollection"})
public class DataFlowAnalysis {

    public void showDataFlowWithInstanceOfExample(Object obj) {
        if (obj instanceof String || obj instanceof Number) {
            System.out.println("String or Number");
        } else {
            System.out.println(obj);
        }
    }

    public void showDataFlowWithOptionalExample() {
        Optional<String> optional = getAnOptional();
        if (optional.isPresent()) {

        }
    }

    public void showDataFlowWithNumericExample(int intValue) {
        if (intValue > 100) {
            return;
        } else if (intValue == 53) {
            System.out.println("This is a special value");
        } else if (intValue == 13) {
            throw new IllegalArgumentException("Invalid value");
        } else if (intValue < -10) {
            throw new IllegalArgumentException("Number too low");
        }

        System.out.println(intValue);
    }

    public void knowsAboutEmptyArrayLists() {
        ArrayList<String> strings = new ArrayList<>();

        String s = strings.get(1);
    }

    private static List<Integer> returnsImmutableResult() {
        return Collections.unmodifiableList(Arrays.asList(1, 2, 3));
    }

    private static void usesUnmodifiableList() {
        returnsImmutableResult().add(4);
    }

    private void unrollsStreamOf() {
        Optional<String> optionalOne = Optional.of("foo");
        Optional<String> optionalTwo = Optional.of("bar");

        if (Stream.of(optionalOne, optionalTwo).allMatch(Optional::isPresent)) {
            String one = optionalOne.get();
        }
    }

    private void shouldKnowValuesForUpToThreeItemsInStreamOf() {
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        if (Stream.of(a, b, c).allMatch(Objects::nonNull)) {
            System.out.println("Present and correct");
        }
    }

    public <T> void newWarningsForIsInstance(@NotNull String property, @NotNull Class<T> clazz) {
        assert clazz == String.class || clazz == Integer.class || clazz == Boolean.class;
        Value value = getValue(property);

        if (clazz.isInstance(value)) {

        }
    }

    private void automaticallyRemoveDoubleNegation(Foo x) {
        if (!(x instanceof Foo)) {
            return;
        }
        System.out.println(x);
    }

    public void hamcrestMatchersSupportedOptional() {
        final Optional<String> foo = getAnOptional();

        assertThat(foo.isPresent(), is(true));
        assertThat(foo.get(), is(42)); // INSPECTION: 'Optional.get()' without 'isPresent()' check
    }

    private void constantEvaluationOfSimpleMethods() {
        String foo = "foo";
        String bar = "bar";

        boolean startsWith = foo.startsWith(bar);
        boolean contains = foo.contains(bar);

        String o = "o";
        boolean lastIndexOf = foo.lastIndexOf(o) == 0;
        //String: contains, indexOf, startsWith, endsWith, lastIndexOf, length, trim, substring, equals, equalsIgnoreCase, charAt, codePointAt, compareTo, replace, valueOf

        int four = 4;
        boolean sqrt = Math.sqrt(four) == 1;
        //Math: abs, sqrt, min, max

    }

    private List<Object> noFalsePositiveAfterIsAssignableFrom(Object value) {
        if (value instanceof List) {
            //do something
        }
        if (Object[].class.isAssignableFrom(value.getClass())) {
            return Arrays.asList((Object[]) value);//warning on cast to Object[]; removing if above, removes the warning
        }
        return null;
    }

    //private helper methods
    private Optional<String> getAnOptional() {
        return Optional.empty();
    }

    private Value getValue(String property) {
        return null;
    }

    //private helper classes
    private class Value {
    }

    interface Foo {}
}