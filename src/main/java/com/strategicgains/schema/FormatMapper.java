package com.strategicgains.schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormatMapper
{
	private static final Map<Class<?>, String> FORMAT_BY_TYPE = new HashMap<>();
	static
	{
		FORMAT_BY_TYPE.put(UUID.class, "uuid");
		FORMAT_BY_TYPE.put(Date.class, "date-time");
		FORMAT_BY_TYPE.put(LocalDate.class, "date");
		FORMAT_BY_TYPE.put(LocalDateTime.class, "date-time");
		FORMAT_BY_TYPE.put(LocalTime.class, "time");
	}

	public static String getFormatFor(Class<?> aClass)
	{
		return FORMAT_BY_TYPE.get(aClass);
	}
}
