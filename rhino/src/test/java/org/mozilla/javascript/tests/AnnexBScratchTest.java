/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mozilla.javascript.testutils.Utils;

/** Scratch test for iterating on Annex B block-scoped function declaration behaviour. */
class AnnexBScratchTest {

    @Test
    void evalBlockFuncSkipWhenLetExists() {
        // Annex B.3.3.3: block function decl in eval should NOT hoist over a let
        Utils.assertWithAllModes_ES6(
                123.0,
                "(function() {\n"
                        + "  var result;\n"
                        + "  eval('let f = 123; { function f() {} } result = f;');\n"
                        + "  return result;\n"
                        + "})()");
    }

    @Test
    void blockFuncSkipWhenLetExists() {
        // Same scenario without eval: let should win over block function decl
        //                        + "  let f = 123;\n"
        Utils.assertWithAllModes_ES6(
                123.0,
                """

                        (function g() {
                        let f = 123;
                          { function f() {return 1}; f() }
                          return f;
                        })(); """);
    }

    @Test
    void letTopLevel() {
        // Same scenario without eval: let should win over block function decl
        //                        + "  let f = 123;\n"
        Utils.assertWithAllModes_ES6(
                2,
                """
                        function f () { return x}
                        function g() {'use strict'; var x = 2; function x() {}; return 2}
                        g()""");
    }

    @Test
    void blockFuncSkipWhenLetExistsAfter() {
        // Annex B conflict check applies regardless of source order:
        // let after block function should still suppress hoisting
        Utils.assertWithAllModes_ES6(
                123.0,
                "(function g() {\n"
                        + "  { function f() {return 1}; }\n"
                        + "  let f = 123;\n"
                        + "  return f;\n"
                        + "})()");
    }

    @Test
    void blockFuncSkipWhenLetInIntermediateBlock() {
        // let in an intermediate block between the function's block and the
        // function scope suppresses Annex B hoisting — f is not defined at
        // function scope.
        Utils.assertWithAllModes_ES6(
                "undefined",
                "(function g() {\n"
                        + "  { { function f() {return 1}; } let f = 123; }\n"
                        + "  return typeof f;\n"
                        + "})()");
    }

    @Test
    void annexBVarHoistUndefinedInFunction() {
        // Annex B: block function decl should create a var-hoisted undefined binding
        Utils.assertWithAllModes_ES6(
                org.mozilla.javascript.Undefined.instance,
                "(function g() {\n"
                        + "  var k = f;\n"
                        + " (function l() {})();"
                        + "  { function f() {return 1}; }\n"
                        + "  return k;\n"
                        + "})()");
    }

    @Test
    void annexBVarHoistUndefinedAtTopLevel() {
        // Same as above but at script level (initScript path)
        Utils.assertWithAllModes_ES6(
                org.mozilla.javascript.Undefined.instance,
                "var k = f;\n" + "{ function f() {return 1}; }\n" + "k;");
    }

    @Disabled("Pre-existing: isValidES6Redeclaration rejects var + block function")
    @Test
    void annexBExistingVarNotOverwritten() {
        // B.3.2.1 step 2: if var f already exists, don't re-initialize to undefined
        Utils.assertWithAllModes_ES6(
                123.0,
                "(function() {\n"
                        + "  var f = 123;\n"
                        + "  var init = f;\n"
                        + "  { function f() {} }\n"
                        + "  return init;\n"
                        + "})()");
    }

    @Disabled("Pre-existing: catch parameter not in symbol table")
    @Test
    void catchDirectFuncRedeclaration() {
        // catch (f) { function f() {} } — direct redeclaration, SyntaxError
        Utils.assertEvaluatorExceptionES6(
                "redeclaration of variable f",
                "(function() {\n"
                        + "  try { throw 1; } catch (f) { function f() { return 2; } }\n"
                        + "})()");
    }

    @Test
    void catchSimpleParamBlockFuncHoists() {
        // catch (f) { { function f() {} } } — nested block, simple catch param,
        // Annex B hoists past catch
        Utils.assertWithAllModes_ES6(
                "function",
                "(function() {\n"
                        + "  try { throw 1; } catch (f) {\n"
                        + "    { function f() { return 2; } }\n"
                        + "  }\n"
                        + "  return typeof f;\n"
                        + "})()");
    }

    @Disabled("Pre-existing: catch destructuring parameter not in symbol table")
    @Test
    void catchDestructuringParamBlocksHoisting() {
        // catch ({ f }) { { function f() {} } } — destructuring catch param,
        // B.3.5: hoisting suppressed
        Utils.assertWithAllModes_ES6(
                "undefined",
                "(function() {\n"
                        + "  try { throw {}; } catch ({ f }) {\n"
                        + "    { function f() {} }\n"
                        + "  }\n"
                        + "  return typeof f;\n"
                        + "})()");
    }

    @Disabled("Pre-existing: catch parameter not in symbol table for let redeclaration")
    @Test
    void catchLetRedeclaration() {
        // catch (f) + let f in same scope should be a SyntaxError
        Utils.assertEvaluatorExceptionES6(
                "redeclaration of variable f", "try { throw 42; } catch (f) { let f = 99; }");
    }

    @Disabled("Pre-existing: isValidES6Redeclaration rejects function + block function")
    @Test
    void annexBNestedBlocksWithFunDecl() {
        // Outer block function is Annex-B applicable, inner is not (would conflict)
        Utils.assertWithAllModes_ES6(
                1.0,
                "function g() {\n"
                        + "  {\n"
                        + "    function f() { return 1; }\n"
                        + "    { function f() { return 2; } }\n"
                        + "  }\n"
                        + "  return f();\n"
                        + "}\n"
                        + "g()");
    }

    @Test
    void annexBSimpleHoist() {
        // Basic Annex B hoisting: block function accessible after block
        Utils.assertWithAllModes_ES6(
                1.0,
                "(function() {\n"
                        + "  { function f() { return 1; } }\n"
                        + "  return f();\n"
                        + "})()");
    }

    @Disabled("Pre-existing: isValidES6Redeclaration rejects var + block function")
    @Test
    void annexBNestedBlocksBothHoist() {
        // Two block functions with same name in nested blocks.
        // Outer should hoist to function scope, inner should not
        // (conflicts with outer's let-like binding).
        Utils.assertWithAllModes_ES6(
                1.0,
                "(function g() {\n"
                        + "  { function f() { return 1; }\n"
                        + "    { function f() { return 2; } }\n"
                        + "  }\n"
                        + "  return f();\n"
                        + "})()");
    }

    @Test
    void annexBHoistInNestedFunction() {
        // Block function in a nested function should hoist to that function's scope
        Utils.assertWithAllModes_ES6(
                5.0,
                "function test() {\n"
                        + "  var r;\n"
                        + "  { r = add(2, 3); function add(a, b) { return a + b; } }\n"
                        + "  return r;\n"
                        + "}\n"
                        + "test()");
    }

    @Test
    void annexBBlockAssignmentDoesNotAffectHoisted() {
        // Assignment to f inside the block hits the block-scoped copy.
        // The var-scoped copy (from step c) is unaffected.
        Utils.assertWithAllModes_ES6(
                "function",
                "(function() {\n"
                        + "  { function f() { return 1; } f = 3; }\n"
                        + "  return typeof f;\n"
                        + "})()");
    }

    @Test
    void annexBVarHoistUndefinedBeforeBlock() {
        // Var-hoisted binding should be undefined before the block is entered.
        // Tests that the annexB symbol makes it into paramAndVarNames for
        // nested functions (not just top-level scripts).
        Utils.assertWithAllModes_ES6(
                org.mozilla.javascript.Undefined.instance,
                "function test() {\n"
                        + "  var init = f;\n"
                        + "  { function f() { return 1; } }\n"
                        + "  return init;\n"
                        + "}\n"
                        + "test()");
    }

    @Test
    void annexBVarHoistNoLocalVars() {
        // Annex B hoisting in an IIFE with no local variables.
        // The var-hoisted binding should still be created.
        Utils.assertWithAllModes_ES6(
                org.mozilla.javascript.Undefined.instance,
                "var k; (function() { k = f; { function f() {} } })(); k;");
    }

    @Test
    void blockFunctionEagerlyInitializedInStrict() {
        // ES6: block function is hoisted to top of block (no TDZ), even in strict mode.
        Utils.assertWithAllModes_ES6(
                "function",
                "'use strict'; (function() {\n"
                        + "  var k;\n"
                        + "  { k = typeof f; function f() {} }\n"
                        + "  return k;\n"
                        + "})()");
    }

    @Test
    void ifDeclSkipWhenLetConflict() {
        // if-body function should not hoist when let with same name exists
        Utils.assertWithAllModes_ES6(
                123.0,
                "(function() {\n"
                        + "  let f = 123;\n"
                        + "  if (true) function f() {}\n"
                        + "  return f;\n"
                        + "})()");
    }

    @Test
    void switchCaseFuncHoist() {
        // Switch-case function should hoist to function scope
        Utils.assertWithAllModes_ES6(
                1.0,
                "(function() {\n"
                        + "  switch(1) { case 1: function f() { return 1; } }\n"
                        + "  return f();\n"
                        + "})()");
    }

    @Test
    void switchCaseFuncSkipWhenLetConflict() {
        // Switch-case function should not hoist when let conflict exists
        Utils.assertWithAllModes_ES6(
                123.0,
                "(function() {\n"
                        + "  let f = 123;\n"
                        + "  switch(1) { case 1: function f() {} }\n"
                        + "  return f;\n"
                        + "})()");
    }

    @Test
    void skipEarlyErrForLoopLet() {
        // for-loop let f conflicts with block function f — no hoisting,
        // f should not be defined after the loop (ReferenceError)
        Utils.assertEcmaErrorES6(
                "ReferenceError: \"f\" is not defined",
                "(function() {\n"
                        + "  for (let f; ; ) {\n"
                        + "    { function f() {} }\n"
                        + "    break;\n"
                        + "  }\n"
                        + "  return f;\n"
                        + "})()");
    }
}
