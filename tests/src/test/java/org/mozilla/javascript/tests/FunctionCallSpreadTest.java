/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.tests;

import org.junit.jupiter.api.Test;
import org.mozilla.javascript.testutils.Utils;

/** Tests for function call spread functionality. */
class FunctionCallSpreadTest {

    @Test
    void testBasicSpread() {
        String script =
                "function sum(a, b, c) { return a + b + c; }"
                        + "var args = [1, 2, 3];"
                        + "sum(...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithLiteralArgs() {
        String script =
                "function sum(a, b, c, d) { return a + b + c + d; }"
                        + "var args = [2, 3];"
                        + "sum(1, ...args, 4);";
        Utils.assertWithAllModes_ES6(10.0, script);
    }

    @Test
    void testMultipleSpreads() {
        String script =
                "function sum(a, b, c, d) { return a + b + c + d; }"
                        + "var args1 = [1, 2];"
                        + "var args2 = [3, 4];"
                        + "sum(...args1, ...args2);";
        Utils.assertWithAllModes_ES6(10.0, script);
    }

    @Test
    void testSpreadAtBeginning() {
        String script =
                "function sum(a, b, c) { return a + b + c; }"
                        + "var args = [1, 2];"
                        + "sum(...args, 3);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadAtEnd() {
        String script =
                "function sum(a, b, c) { return a + b + c; }"
                        + "var args = [2, 3];"
                        + "sum(1, ...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testEmptySpread() {
        String script =
                "function sum(a, b) { return (a || 0) + (b || 0); }"
                        + "var args = [];"
                        + "sum(...args);";
        Utils.assertWithAllModes_ES6(0.0, script);
    }

    @Test
    void testSpreadWithMoreArgsThanParams() {
        String script =
                "function first(a) { return a; }"
                        + "var args = [1, 2, 3, 4, 5];"
                        + "first(...args);";
        Utils.assertWithAllModes_ES6(1, script);
    }

    @Test
    void testSpreadWithArgumentsObject() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var args = [1, 2, 3];"
                        + "collect(...args);";
        Utils.assertWithAllModes_ES6("1,2,3", script);
    }

    @Test
    void testSpreadMethodCall() {
        String script =
                "var obj = {"
                        + "  sum: function(a, b, c) { return a + b + c; }"
                        + "};"
                        + "var args = [1, 2, 3];"
                        + "obj.sum(...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithStrings() {
        String script =
                "function concat(a, b, c) { return a + b + c; }"
                        + "var args = ['hello', ' ', 'world'];"
                        + "concat(...args);";
        Utils.assertWithAllModes_ES6("hello world", script);
    }

    @Test
    void testSpreadMixedTypes() {
        String script =
                "function join(a, b, c) { return '' + a + b + c; }"
                        + "var args = [1, 'x', true];"
                        + "join(...args);";
        Utils.assertWithAllModes_ES6("1xtrue", script);
    }

    // Multiple spread nodes tests
    @Test
    void testThreeSpreads() {
        String script =
                "function sum(a, b, c, d, e, f) { return a + b + c + d + e + f; }"
                        + "var a1 = [1, 2];"
                        + "var a2 = [3, 4];"
                        + "var a3 = [5, 6];"
                        + "sum(...a1, ...a2, ...a3);";
        Utils.assertWithAllModes_ES6(21.0, script);
    }

    @Test
    void testMultipleSpreadsWithLiteralsBetween() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var a1 = [1, 2];"
                        + "var a2 = [4, 5];"
                        + "collect(...a1, 3, ...a2, 6);";
        Utils.assertWithAllModes_ES6("1,2,3,4,5,6", script);
    }

    @Test
    void testSpreadLiteralSpreadLiteral() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var arr = [2, 3];"
                        + "collect(1, ...arr, 4);";
        Utils.assertWithAllModes_ES6("1,2,3,4", script);
    }

    @Test
    void testLiteralSpreadLiteralSpread() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var a1 = [2];"
                        + "var a2 = [4];"
                        + "collect(1, ...a1, 3, ...a2);";
        Utils.assertWithAllModes_ES6("1,2,3,4", script);
    }

    @Test
    void testSpreadBetweenLiterals() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var arr = [2, 3];"
                        + "collect(1, ...arr, 4, 5);";
        Utils.assertWithAllModes_ES6("1,2,3,4,5", script);
    }

    @Test
    void testMultipleEmptySpreads() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var e1 = [];"
                        + "var e2 = [];"
                        + "collect(...e1, 1, ...e2, 2);";
        Utils.assertWithAllModes_ES6("1,2", script);
    }

    @Test
    void testConsecutiveSpreads() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var a1 = [1];"
                        + "var a2 = [2];"
                        + "var a3 = [3];"
                        + "collect(...a1, ...a2, ...a3);";
        Utils.assertWithAllModes_ES6("1,2,3", script);
    }

    @Test
    void testOnlyLiteralsNoSpread() {
        // Make sure regular calls still work
        String script = "function sum(a, b, c) { return a + b + c; }" + "sum(1, 2, 3);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithIterator() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var obj = {"
                        + "  *[Symbol.iterator]() {"
                        + "    yield 'a';"
                        + "    yield 'b';"
                        + "  }"
                        + "};"
                        + "collect(1, ...obj, 2);";
        Utils.assertWithAllModes_ES6("1,a,b,2", script);
    }

    @Test
    void testSpreadOnString() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "collect(...'abc');";
        Utils.assertWithAllModes_ES6("a,b,c", script);
    }

    @Test
    void testSpreadPreservesOrder() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join('-'); }"
                        + "var a = [1, 2];"
                        + "var b = [3, 4];"
                        + "collect(0, ...a, ...b, 5);";
        Utils.assertWithAllModes_ES6("0-1-2-3-4-5", script);
    }

    // Chaining tests
    @Test
    void testSpreadWithChainedMethodCall() {
        String script =
                "var obj = {"
                        + "  nums: [],"
                        + "  add: function(a, b, c) { this.nums.push(a, b, c); return this; },"
                        + "  mul: function(x) { this.nums = this.nums.map(function(n) { return n * x; }); return this; },"
                        + "  result: function() { return this.nums.join(','); }"
                        + "};"
                        + "var args = [1, 2, 3];"
                        + "obj.add(...args).mul(2).result();";
        Utils.assertWithAllModes_ES6("2,4,6", script);
    }

    @Test
    void testSpreadReturnValueAsSpread() {
        // Result of function call used as spread source
        String script =
                "function getArgs() { return [1, 2, 3]; }"
                        + "function sum(a, b, c) { return a + b + c; }"
                        + "sum(...getArgs());";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadNestedFunctionCalls() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments); }"
                        + "function wrap(arr) { return ['start'].concat(arr).concat(['end']).join(','); }"
                        + "var args = [1, 2, 3];"
                        + "wrap(collect(...args));";
        Utils.assertWithAllModes_ES6("start,1,2,3,end", script);
    }

    // Generator function tests
    @Test
    void testSpreadGeneratorFunction() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "function* gen() {"
                        + "  yield 1;"
                        + "  yield 2;"
                        + "  yield 3;"
                        + "}"
                        + "collect(...gen());";
        Utils.assertWithAllModes_ES6("1,2,3", script);
    }

    @Test
    void testSpreadGeneratorWithLiterals() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "function* gen() {"
                        + "  yield 'b';"
                        + "  yield 'c';"
                        + "}"
                        + "collect('a', ...gen(), 'd');";
        Utils.assertWithAllModes_ES6("a,b,c,d", script);
    }

    @Test
    void testSpreadEmptyGenerator() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "function* gen() {}"
                        + "collect(1, ...gen(), 2);";
        Utils.assertWithAllModes_ES6("1,2", script);
    }

    @Test
    void testSpreadMultipleGenerators() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "function* gen1() { yield 1; yield 2; }"
                        + "function* gen2() { yield 3; yield 4; }"
                        + "collect(...gen1(), ...gen2());";
        Utils.assertWithAllModes_ES6("1,2,3,4", script);
    }

    // Custom iterator tests
    @Test
    void testSpreadCustomIterator() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var counter = {"
                        + "  max: 3,"
                        + "  [Symbol.iterator]: function() {"
                        + "    var count = 0;"
                        + "    var self = this;"
                        + "    return {"
                        + "      next: function() {"
                        + "        count++;"
                        + "        if (count <= self.max) {"
                        + "          return { value: count, done: false };"
                        + "        }"
                        + "        return { done: true };"
                        + "      }"
                        + "    };"
                        + "  }"
                        + "};"
                        + "collect(...counter);";
        Utils.assertWithAllModes_ES6("1,2,3", script);
    }

    @Test
    void testSpreadSet() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).join(','); }"
                        + "var s = new Set([1, 2, 3]);"
                        + "collect(...s);";
        Utils.assertWithAllModes_ES6("1,2,3", script);
    }

    @Test
    void testSpreadMap() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).map(function(e) { return e.join(':'); }).join(','); }"
                        + "var m = new Map([['a', 1], ['b', 2]]);"
                        + "collect(...m);";
        Utils.assertWithAllModes_ES6("a:1,b:2", script);
    }

    // Error handling tests
    @Test
    void testSpreadIteratorThrowsOnInit() {
        String script =
                "function fn() {}"
                        + "var obj = {"
                        + "  [Symbol.iterator]: function() {"
                        + "    throw new Error('Iterator init failed');"
                        + "  }"
                        + "};"
                        + "try {"
                        + "  fn(...obj);"
                        + "  'should not reach';"
                        + "} catch (e) {"
                        + "  e.message;"
                        + "}";
        Utils.assertWithAllModes_ES6("Iterator init failed", script);
    }

    @Test
    void testSpreadIteratorThrowsOnNext() {
        String script =
                "function fn() {}"
                        + "var obj = {"
                        + "  [Symbol.iterator]: function() {"
                        + "    return {"
                        + "      next: function() {"
                        + "        throw new Error('Next failed');"
                        + "      }"
                        + "    };"
                        + "  }"
                        + "};"
                        + "try {"
                        + "  fn(...obj);"
                        + "  'should not reach';"
                        + "} catch (e) {"
                        + "  e.message;"
                        + "}";
        Utils.assertWithAllModes_ES6("Next failed", script);
    }

    // Nested arrays and complex objects
    @Test
    void testSpreadNestedArrays() {
        // Spread doesn't flatten nested arrays, just passes them as args
        String script =
                "function collect() { return Array.prototype.slice.call(arguments); }"
                        + "var arr = [[1, 2], [3, 4]];"
                        + "var result = collect(...arr);"
                        + "result.length + ',' + result[0].join('-') + ',' + result[1].join('-');";
        Utils.assertWithAllModes_ES6("2,1-2,3-4", script);
    }

    @Test
    void testSpreadWithUndefinedInArray() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).map(function(x) { return x === undefined ? 'undef' : x; }).join(','); }"
                        + "var arr = [1, undefined, 3];"
                        + "collect(...arr);";
        Utils.assertWithAllModes_ES6("1,undef,3", script);
    }

    @Test
    void testSpreadWithNullInArray() {
        String script =
                "function collect() { return Array.prototype.slice.call(arguments).map(function(x) { return x === null ? 'null' : x; }).join(','); }"
                        + "var arr = [1, null, 3];"
                        + "collect(...arr);";
        Utils.assertWithAllModes_ES6("1,null,3", script);
    }

    // Apply-like behavior tests
    @Test
    void testSpreadWithThisBinding() {
        String script =
                "var obj = {"
                        + "  multiplier: 2,"
                        + "  multiply: function(a, b, c) { return (a + b + c) * this.multiplier; }"
                        + "};"
                        + "var args = [1, 2, 3];"
                        + "obj.multiply(...args);";
        Utils.assertWithAllModes_ES6(12.0, script);
    }

    // Large spread
    @Test
    void testSpreadLargeArray() {
        String script =
                "function sum() {"
                        + "  var total = 0;"
                        + "  for (var i = 0; i < arguments.length; i++) total += arguments[i];"
                        + "  return total;"
                        + "}"
                        + "var arr = [];"
                        + "for (var i = 1; i <= 100; i++) arr.push(i);"
                        + "sum(...arr);";
        // Sum of 1 to 100 = 5050
        Utils.assertWithAllModes_ES6(5050.0, script);
    }

    // ES5 should not support spread
    @Test
    void testSpreadUnsupportedInES5() {
        String script = "function fn() {} fn(...[1, 2, 3]);";
        Utils.assertEvaluatorException_1_8("syntax error (test#1)", script);
    }

    // Bound function tests
    @Test
    void testSpreadWithBoundFunction() {
        String script =
                "function sum(a, b, c) { return a + b + c; }"
                        + "var boundSum = sum.bind(null);"
                        + "var args = [1, 2, 3];"
                        + "boundSum(...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithBoundFunctionPreboundArgs() {
        String script =
                "function sum(a, b, c, d) { return a + b + c + d; }"
                        + "var boundSum = sum.bind(null, 1);"
                        + "var args = [2, 3, 4];"
                        + "boundSum(...args);";
        Utils.assertWithAllModes_ES6(10.0, script);
    }

    @Test
    void testSpreadWithBoundFunctionThis() {
        String script =
                "var obj = { x: 10 };"
                        + "function addX(a, b) { return this.x + a + b; }"
                        + "var bound = addX.bind(obj);"
                        + "var args = [1, 2];"
                        + "bound(...args);";
        Utils.assertWithAllModes_ES6(13.0, script);
    }

    @Test
    void testSpreadWithDoubleBoundFunction() {
        String script =
                "function sum(a, b, c) { return a + b + c; }"
                        + "var bound1 = sum.bind(null, 1);"
                        + "var bound2 = bound1.bind(null, 2);"
                        + "var args = [3];"
                        + "bound2(...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    // Apply and call tests
    @Test
    void testSpreadWithFunctionCall() {
        String script =
                "function sum(a, b, c) { return a + b + c; }"
                        + "var args = [1, 2, 3];"
                        + "sum.call(null, ...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithFunctionCallMixed() {
        String script =
                "function sum(a, b, c, d) { return a + b + c + d; }"
                        + "var args = [2, 3];"
                        + "sum.call(null, 1, ...args, 4);";
        Utils.assertWithAllModes_ES6(10.0, script);
    }

    @Test
    void testSpreadWithFunctionApply() {
        // Note: apply takes an array-like argument, so spread + apply is rare
        // but should still work
        String script =
                "function sum(a, b) { return a + b; }"
                        + "var args = [null, [1, 2]];"
                        + "sum.apply(...args);";
        Utils.assertWithAllModes_ES6(3.0, script);
    }

    // Arrow function tests
    @Test
    void testSpreadWithArrowFunction() {
        String script =
                "var sum = (a, b, c) => a + b + c;" + "var args = [1, 2, 3];" + "sum(...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithArrowFunctionImmediate() {
        String script = "((a, b, c) => a + b + c)(...[1, 2, 3]);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithArrowInObject() {
        String script =
                "var obj = {"
                        + "  x: 10,"
                        + "  add: (a, b) => a + b"
                        + "};"
                        + "var args = [1, 2];"
                        + "obj.add(...args);";
        Utils.assertWithAllModes_ES6(3.0, script);
    }

    // Super method call tests
    @Test
    void testSpreadWithSuperMethodCall() {
        // super requires shorthand method syntax
        String script =
                "var proto = {"
                        + "  sum(a, b, c) { return a + b + c; }"
                        + "};"
                        + "var obj = {"
                        + "  callSuper() {"
                        + "    var args = [1, 2, 3];"
                        + "    return super.sum(...args);"
                        + "  }"
                        + "};"
                        + "Object.setPrototypeOf(obj, proto);"
                        + "obj.callSuper();";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithSuperMethodCallMixed() {
        String script =
                "var proto = {"
                        + "  sum(a, b, c, d) { return a + b + c + d; }"
                        + "};"
                        + "var obj = {"
                        + "  callSuper() {"
                        + "    var args = [2, 3];"
                        + "    return super.sum(1, ...args, 4);"
                        + "  }"
                        + "};"
                        + "Object.setPrototypeOf(obj, proto);"
                        + "obj.callSuper();";
        Utils.assertWithAllModes_ES6(10.0, script);
    }

    @Test
    void testSpreadWithSuperMethodCallPreservesThis() {
        String script =
                "var proto = {"
                        + "  getValue(mult) { return this.x * mult; }"
                        + "};"
                        + "var obj = {"
                        + "  x: 10,"
                        + "  callSuper() {"
                        + "    var args = [5];"
                        + "    return super.getValue(...args);"
                        + "  }"
                        + "};"
                        + "Object.setPrototypeOf(obj, proto);"
                        + "obj.callSuper();";
        Utils.assertWithAllModes_ES6(50.0, script);
    }

    // Combined scenarios
    @Test
    void testSpreadWithBoundArrowFunction() {
        // Arrow functions ignore bind's thisArg, but bound args work
        String script =
                "var add = (a, b, c) => a + b + c;"
                        + "var bound = add.bind(null, 1);"
                        + "var args = [2, 3];"
                        + "bound(...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }

    @Test
    void testSpreadWithBoundFunctionAndCall() {
        String script =
                "function sum(a, b, c) { return a + b + c; }"
                        + "var bound = sum.bind(null, 1);"
                        + "var args = [2, 3];"
                        + "bound.call(null, ...args);";
        Utils.assertWithAllModes_ES6(6.0, script);
    }
}
