/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.engine.backend.types.dsl;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import org.hibernate.search.engine.spatial.GeoPoint;


/**
 * @author Yoann Rodiere
 */
public interface IndexFieldTypeFactoryContext {

	<F> StandardIndexFieldTypeContext<?, F> as(Class<F> inputType);

	StringIndexFieldTypeContext<?> asString();

	StandardIndexFieldTypeContext<?, Integer> asInteger();

	StandardIndexFieldTypeContext<?, Long> asLong();

	StandardIndexFieldTypeContext<?, Boolean> asBoolean();

	StandardIndexFieldTypeContext<?, Character> asCharacter();

	StandardIndexFieldTypeContext<?, Byte> asByte();

	StandardIndexFieldTypeContext<?, Short> asShort();

	StandardIndexFieldTypeContext<?, Float> asFloat();

	StandardIndexFieldTypeContext<?, Double> asDouble();

	StandardIndexFieldTypeContext<?, LocalDate> asLocalDate();

	StandardIndexFieldTypeContext<?, LocalDateTime> asLocalDateTime();

	StandardIndexFieldTypeContext<?, LocalTime> asLocalTime();

	StandardIndexFieldTypeContext<?, Instant> asInstant();

	StandardIndexFieldTypeContext<?, ZonedDateTime> asZonedDateTime();

	StandardIndexFieldTypeContext<?, Year> asYear();

	StandardIndexFieldTypeContext<?, YearMonth> asYearMonth();

	StandardIndexFieldTypeContext<?, GeoPoint> asGeoPoint();

	StandardIndexFieldTypeContext<?, URI> asUri();

	// TODO NumericBridgeProvider
	// TODO JavaTimeBridgeProvider
	// TODO BasicJDKTypesBridgeProvider

	default <T> T extension(IndexFieldTypeFactoryContextExtension<T> extension) {
		return extension.extendOrFail( this );
	}

}