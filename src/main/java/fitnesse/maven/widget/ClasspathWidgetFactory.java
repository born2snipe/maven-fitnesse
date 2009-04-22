/**
 * Copyright to the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package fitnesse.maven.widget;

import fitnesse.maven.util.StringUtil;
import fitnesse.wikitext.widgets.ClasspathWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.io.File;
import java.util.List;


public class ClasspathWidgetFactory {

    public void build(ParentWidget parentWidget, List<String> dependencies) throws Exception {
        new ClasspathWidget(parentWidget, String.format("%s %s", "!path", StringUtil.join(File.pathSeparator, dependencies)));
    }

    public void build(ParentWidget parentWidget, String dependency) throws Exception {
        new ClasspathWidget(parentWidget, String.format("%s %s", "!path", dependency));
    }
}
