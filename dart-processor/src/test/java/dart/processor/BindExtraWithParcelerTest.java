/*
 * Copyright 2013 Jake Wharton
 * Copyright 2014 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dart.processor;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.google.common.base.Joiner;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

/**
 * Tests {@link dart.processor.InjectExtraProcessor}. For tests related to Parceler and Parceler is
 * available.
 */
public class BindExtraWithParcelerTest {

  @Test
  public void serializableCollection() {
    JavaFileObject source =
        JavaFileObjects.forSourceString(
            "test.TestSerializableCollection",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import android.app.Activity;", //
                    "import dart.BindExtra;", //
                    "import java.lang.Object;", //
                    "import java.lang.String;", //
                    "import android.util.SparseArray;", //
                    "public class TestSerializableCollection extends Activity {", //
                    "  @BindExtra(\"key\") SparseArray<String> extra;", //
                    "}" //
                    ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString(
            "test/TestSerializableCollection__ExtraBinder",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import dart.Dart;", //
                    "import java.lang.Object;", //
                    "public class TestSerializableCollection__ExtraBinder {", //
                    "  public static void bind(Dart.Finder finder, TestSerializableCollection target, Object source) {", //
                    "    Object object;", //
                    "    object = finder.getExtra(source, \"key\");", //
                    "    if (object == null) {", //
                    "      throw new IllegalStateException(\"Required extra with key 'key' for field 'extra' was not found. If this extra is optional add '@Nullable' annotation.\");", //
                    "    }", //
                    "    target.extra = org.parceler.Parcels.unwrap((android.os.Parcelable) object);", //
                    "  }", //
                    "}" //
                    ));

    Compilation compilation =
        javac().withProcessors(ProcessorTestUtilities.dartProcessors()).compile(source);
    assertThat(compilation)
        .generatedSourceFile("test/TestSerializableCollection__ExtraBinder")
        .hasSourceEquivalentTo(expectedSource);
  }

  @Test
  public void nonSerializableNonParcelableCollection_withoutParceler() {
    JavaFileObject source =
        JavaFileObjects.forSourceString(
            "test.TestNonSerializableNonParcelableCollection_withoutParceler",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import android.app.Activity;", //
                    "import dart.BindExtra;", //
                    "import java.lang.Object;", //
                    "import java.lang.String;", //
                    "import java.util.List;", //
                    "public class TestNonSerializableNonParcelableCollection_withoutParceler extends Activity {", //
                    "  @BindExtra(\"key\") List<String> extra;", //
                    "}" //
                    ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString(
            "test/TestNonSerializableNonParcelableCollection_withoutParceler__ExtraBinder",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import dart.Dart;", //
                    "import java.lang.Object;", //
                    "public class TestNonSerializableNonParcelableCollection_withoutParceler__ExtraBinder {", //
                    "  public static void bind(Dart.Finder finder, TestNonSerializableNonParcelableCollection_withoutParceler target, Object source) {", //
                    "    Object object;", //
                    "    object = finder.getExtra(source, \"key\");", //
                    "    if (object == null) {", //
                    "      throw new IllegalStateException(\"Required extra with key 'key' for field 'extra' was not found. If this extra is optional add '@Nullable' annotation.\");", //
                    "    }", //
                    "    target.extra = org.parceler.Parcels.unwrap((android.os.Parcelable) object);", //
                    "  }", //
                    "}"));

    Compilation compilation =
        javac().withProcessors(ProcessorTestUtilities.dartProcessors()).compile(source);
    assertThat(compilation)
        .generatedSourceFile(
            "test/TestNonSerializableNonParcelableCollection_withoutParceler__ExtraBinder")
        .hasSourceEquivalentTo(expectedSource);
  }

  @Test
  public void parcelAnnotatedType() {
    JavaFileObject source =
        JavaFileObjects.forSourceString(
            "test.TestParcelAnnotated",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import android.app.Activity;", //
                    "import dart.BindExtra;", //
                    "import java.lang.Object;", //
                    "import java.lang.String;", //
                    "import org.parceler.Parcel;", //
                    "public class TestParcelAnnotated extends Activity {", //
                    "  @BindExtra(\"key\") Foo extra;", //
                    "@Parcel static class Foo {}", //
                    "}"));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString(
            "test/TestParcelAnnotated__ExtraBinder",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import dart.Dart;", //
                    "import java.lang.Object;", //
                    "public class TestParcelAnnotated__ExtraBinder {", //
                    "  public static void bind(Dart.Finder finder, TestParcelAnnotated target, Object source) {", //
                    "    Object object;", //
                    "    object = finder.getExtra(source, \"key\");", //
                    "    if (object == null) {", //
                    "      throw new IllegalStateException(\"Required extra with key 'key' for field 'extra' was not found. If this extra is optional add '@Nullable' annotation.\");", //
                    "    }", //
                    "    target.extra = org.parceler.Parcels.unwrap((android.os.Parcelable) object);", //
                    "  }", //
                    "}" //
                    ));

    Compilation compilation =
        javac().withProcessors(ProcessorTestUtilities.dartProcessors()).compile(source);
    assertThat(compilation)
        .generatedSourceFile("test/TestParcelAnnotated__ExtraBinder")
        .hasSourceEquivalentTo(expectedSource);
  }

  @Test
  public void collectionOfParcelAnnotatedType() {
    JavaFileObject source =
        JavaFileObjects.forSourceString(
            "test.TestCollectionParcel",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import android.app.Activity;", //
                    "import dart.BindExtra;", //
                    "import java.lang.Object;", //
                    "import java.lang.String;", //
                    "import java.util.List;", //
                    "import org.parceler.Parcel;", //
                    "public class TestCollectionParcel extends Activity {", //
                    "  @BindExtra(\"key\") List<Foo> extra;", //
                    "@Parcel static class Foo {}", //
                    "}"));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString(
            "test/TestCollectionParcel__ExtraBinder",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import dart.Dart;", //
                    "import java.lang.Object;", //
                    "public class TestCollectionParcel__ExtraBinder {", //
                    "  public static void bind(Dart.Finder finder, TestCollectionParcel target, Object source) {", //
                    "    Object object;", //
                    "    object = finder.getExtra(source, \"key\");", //
                    "    if (object == null) {", //
                    "      throw new IllegalStateException(\"Required extra with key 'key' for field 'extra' was not found. If this extra is optional add '@Nullable' annotation.\");", //
                    "    }", //
                    "    target.extra = org.parceler.Parcels.unwrap((android.os.Parcelable) object);", //
                    "  }", //
                    "}" //
                    ));

    Compilation compilation =
        javac().withProcessors(ProcessorTestUtilities.dartProcessors()).compile(source);
    assertThat(compilation)
        .generatedSourceFile("test/TestCollectionParcel__ExtraBinder")
        .hasSourceEquivalentTo(expectedSource);
  }

  @Test
  public void bindingParcelThatExtendsParcelableExtra() {
    JavaFileObject source =
        JavaFileObjects.forSourceString(
            "test.TestParcelExtendsParcelable",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import android.app.Activity;", //
                    "import android.os.Parcelable;", //
                    "import org.parceler.Parcel;", //
                    "import dart.BindExtra;", //
                    "class ExtraParent implements Parcelable {", //
                    "  public void writeToParcel(android.os.Parcel out, int flags) {", //
                    "  }", //
                    "  public int describeContents() {", //
                    "    return 0;", //
                    "  }", //
                    "}", //
                    "@Parcel class Extra extends ExtraParent {}", //
                    "public class TestParcelExtendsParcelable extends Activity {", //
                    "    @BindExtra(\"key\") Extra extra;", //
                    "}" //
                    ));

    JavaFileObject builderSource =
        JavaFileObjects.forSourceString(
            "test/TestParcelExtendsParcelable__ExtraBinder",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import dart.Dart;", //
                    "import java.lang.Object;", //
                    "public class TestParcelExtendsParcelable__ExtraBinder {", //
                    "  public static void bind(Dart.Finder finder, TestParcelExtendsParcelable target, Object source) {", //
                    "    Object object;", //
                    "    object = finder.getExtra(source, \"key\");", //
                    "    if (object == null) {", //
                    "      throw new IllegalStateException(\"Required extra with key 'key' for field 'extra' was not found. If this extra is optional add '@Nullable' annotation.\");", //
                    "    }", //
                    "    target.extra = org.parceler.Parcels.unwrap((android.os.Parcelable) object);", //
                    "  }", //
                    "}" //
                    ));

    Compilation compilation =
        javac().withProcessors(ProcessorTestUtilities.dartProcessors()).compile(source);
    assertThat(compilation)
        .generatedSourceFile("test/TestParcelExtendsParcelable__ExtraBinder")
        .hasSourceEquivalentTo(builderSource);
  }

  @Test
  public void bindingParcelableThatExtendsParcelableExtra() {
    JavaFileObject source =
        JavaFileObjects.forSourceString(
            "test.TestParcelableExtendsParcelable",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import android.app.Activity;", //
                    "import android.os.Parcelable;", //
                    "import dart.BindExtra;", //
                    "class ExtraParent implements Parcelable {", //
                    "  public void writeToParcel(android.os.Parcel out, int flags) {", //
                    "  }", //
                    "  public int describeContents() {", //
                    "    return 0;", //
                    "  }", //
                    "}", //
                    "class Extra extends ExtraParent implements Parcelable {", //
                    "  public void writeToParcel(android.os.Parcel out, int flags) {", //
                    "  }", //
                    "  public int describeContents() {", //
                    "    return 0;", //
                    "  }", //
                    "}", //
                    "public class TestParcelableExtendsParcelable extends Activity {", //
                    "    @BindExtra(\"key\") Extra extra;", //
                    "}" //
                    ));

    JavaFileObject builderSource =
        JavaFileObjects.forSourceString(
            "test/TestParcelableExtendsParcelable__ExtraBinder",
            Joiner.on('\n')
                .join( //
                    "package test;", //
                    "import dart.Dart;", //
                    "import java.lang.Object;", //
                    "public class TestParcelableExtendsParcelable__ExtraBinder {", //
                    "  public static void bind(Dart.Finder finder, TestParcelableExtendsParcelable target, Object source) {", //
                    "    Object object;", //
                    "    object = finder.getExtra(source, \"key\");", //
                    "    if (object == null) {", //
                    "      throw new IllegalStateException(\"Required extra with key 'key' for field 'extra' was not found. If this extra is optional add '@Nullable' annotation.\");", //
                    "    }", //
                    "    target.extra = (Extra) object;", //
                    "  }", //
                    "}" //
                    ));

    Compilation compilation =
        javac().withProcessors(ProcessorTestUtilities.dartProcessors()).compile(source);
    assertThat(compilation)
        .generatedSourceFile("test/TestParcelableExtendsParcelable__ExtraBinder")
        .hasSourceEquivalentTo(builderSource);
  }
}
