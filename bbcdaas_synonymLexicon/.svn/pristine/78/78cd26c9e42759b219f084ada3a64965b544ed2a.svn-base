/* Licensed to the Apache Software Foundation (ASF) under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership. The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
*/

package de.bbcdaas.synonymlexicon.common.mahout.lib;

/*
 * Copyright 1999 CERN - European Organization for Nuclear Research.
 * Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose
 * is hereby granted without fee, provided that the above copyright notice appear in all copies and
 * that both that copyright notice and this permission notice appear in supporting documentation.
 * CERN makes no representations about the suitability of this software for any purpose.
 * It is provided "as is" without expressed or implied warranty.
*/

public final class Mult implements DoubleFunction {

	private double multiplicator;

	Mult(double multiplicator) {
		this.multiplicator = multiplicator;
	}

	@Override
	public double apply(double a) {
		return a * multiplicator;
	}

	public static Mult div(double constant) {
		return mult(1 / constant);
	}

	public static Mult mult(double constant) {
		return new Mult(constant);
	}

	public double getMultiplicator() {
		return multiplicator;
	}

	public void setMultiplicator(double multiplicator) {
		this.multiplicator = multiplicator;
	}
}