package com.isyll.password_manager.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateTimeUtils {

	public static ZonedDateTime getCurrentTimestamp() {
		return ZonedDateTime.now(ZoneOffset.UTC);
	}
}