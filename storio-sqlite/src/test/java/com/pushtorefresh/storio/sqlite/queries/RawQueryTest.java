package com.pushtorefresh.storio.sqlite.queries;

import com.google.common.collect.HashMultiset;
import com.pushtorefresh.storio.test.ToStringChecker;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class RawQueryTest {

    @Test
    public void shouldNotAllowNullQuery() {
        try {
            //noinspection ConstantConditions
            RawQuery.builder()
                    .query(null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException expected) {
            assertThat(expected)
                    .hasMessage("Query is null or empty")
                    .hasNoCause();
        }
    }

    @Test
    public void shouldNotAllowEmptyQuery() {
        try {
            RawQuery.builder()
                    .query("");
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException expected) {
            assertThat(expected)
                    .hasMessage("Query is null or empty")
                    .hasNoCause();
        }
    }

    @Test
    public void argsShouldNotBeNull() {
        RawQuery rawQuery = RawQuery.builder()
                .query("lalala I know SQL")
                .build();

        assertThat(rawQuery.args()).isNotNull();
        assertThat(rawQuery.args()).isEmpty();
    }

    @Test
    public void observesTablesShouldNotBeNull() {
        RawQuery rawQuery = RawQuery.builder()
                .query("lalala I know SQL")
                .build();

        assertThat(rawQuery.observesTables()).isNotNull();
        assertThat(rawQuery.observesTables()).isEmpty();
    }

    @Test
    public void affectsTablesShouldNotBeNull() {
        RawQuery rawQuery = RawQuery.builder()
                .query("lalala I know SQL")
                .build();

        assertThat(rawQuery.affectsTables()).isNotNull();
        assertThat(rawQuery.affectsTables()).isEmpty();
    }

    @Test
    public void shouldRewriteAffectsTablesOnSecondCall() {
        RawQuery rawQuery = RawQuery.builder()
                .query("test_query")
                .affectsTables("first_call")
                .affectsTables("second_call")
                .build();

        assertThat(rawQuery.affectsTables()).isEqualTo(singleton("second_call"));
    }

    @Test
    public void shouldRewriteObservesTablesOnSecondCall() {
        RawQuery rawQuery = RawQuery.builder()
                .query("test_query")
                .observesTables("first_call")
                .observesTables("second_call")
                .build();

        assertThat(rawQuery.observesTables()).isEqualTo(singleton("second_call"));
    }

    @Test
    public void completeBuilderShouldNotAllowNullQuery() {
        try {
            //noinspection ConstantConditions
            RawQuery.builder()
                    .query("test_query")
                    .query(null);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException expected) {
            assertThat(expected)
                    .hasMessage("Query is null or empty")
                    .hasNoCause();
        }
    }

    @Test
    public void completeBuilderShouldNotAllowEmptyQuery() {
        try {
            RawQuery.builder()
                    .query("test_query")
                    .query("");
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException expected) {
            assertThat(expected)
                    .hasMessage("Query is null or empty")
                    .hasNoCause();
        }
    }

    @Test
    public void completeBuilderShouldUpdateTable() {
        RawQuery rawQuery = RawQuery.builder()
                .query("old_query")
                .query("new_query")
                .build();

        assertThat(rawQuery.query()).isEqualTo("new_query");
    }

    @Test
    public void createdThroughToBuilderQueryShouldBeEqual() {
        final String query = "test_query";
        final Object[] args = {"arg1", "arg2", "arg3"};
        final String[] observesTables = {"table_to_observe_1", "table_to_observe_2"};
        final String[] affectsTables = {"table_to_affect_1", "table_to_affect_2"};

        final RawQuery firstQuery = RawQuery.builder()
                .query(query)
                .args(args)
                .observesTables(observesTables)
                .affectsTables(affectsTables)
                .build();

        final RawQuery secondQuery = firstQuery.toBuilder().build();

        assertThat(secondQuery).isEqualTo(firstQuery);
    }

    @Test
    public void buildWithNormalValues() {
        final String query = "test_query";
        final Object[] args = {"arg1", "arg2", "arg3"};
        final String[] observesTables = {"table_to_observe_1", "table_to_observe_2"};
        final String[] affectsTables = {"table_to_affect_1", "table_to_affect_2"};

        final RawQuery rawQuery = RawQuery.builder()
                .query(query)
                .args(args)
                .observesTables(observesTables)
                .affectsTables(affectsTables)
                .build();

        assertThat(rawQuery.query()).isEqualTo(query);
        assertThat(rawQuery.args()).isEqualTo(asList(args));
        assertThat(HashMultiset.create(rawQuery.observesTables())).isEqualTo(HashMultiset.create(asList(observesTables)));
        assertThat(HashMultiset.create(rawQuery.affectsTables())).isEqualTo(HashMultiset.create(asList(affectsTables)));
    }

    @Test
    public void verifyEqualsAndHashCodeImplementation() {
        EqualsVerifier
                .forClass(RawQuery.class)
                .allFieldsShouldBeUsed()
                .verify();
    }

    @Test
    public void checkToStringImplementation() {
        ToStringChecker
                .forClass(RawQuery.class)
                .check();
    }
}
