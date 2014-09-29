package com.km.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class PasswordUtilTest {

	@Test
	public void encode() {
		Assert.assertEquals(PasswordUtil.encode("123456"), PasswordUtil.encode("123456"));
		Assert.assertNotEquals(PasswordUtil.encode("QM@hn$lfjk"), PasswordUtil.encode("ABC1234"));
		Assert.assertEquals(PasswordUtil.encode("2323232FDFDLKLKDO93JU7J"), PasswordUtil.encode("2323232FDFDLKLKDO93JU7J"));
	}
}
