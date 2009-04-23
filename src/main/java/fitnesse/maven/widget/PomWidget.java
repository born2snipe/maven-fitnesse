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

import fitnesse.html.HtmlElement;
import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.html.RawHtml;
import fitnesse.maven.util.FileUtil;
import fitnesse.maven.util.MavenDependencyResolver;
import fitnesse.maven.util.MavenException;
import fitnesse.wiki.PageData;
import fitnesse.wikitext.WidgetBuilder;
import fitnesse.wikitext.widgets.ClasspathWidget;
import fitnesse.wikitext.widgets.ParentWidget;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PomWidget extends ClasspathWidget {

	public static final String REGEXP = "^!pom [^\r\n]*";
	protected static ClasspathWidgetFactory CLASSPATH_WIDGET_FACTORY = new ClasspathWidgetFactory();
	protected static FileUtil FILE_UTIL = new FileUtil();
	protected static MavenDependencyResolver MAVEN_DEPENDENCY_RESOLVER = new MavenDependencyResolver();
	private static final Pattern pattern = Pattern.compile("^!pom (.*)", Pattern.CASE_INSENSITIVE);
	private static final String collapsableClosedCss = "hidden";
	private static final String collapsableClosedImg = "/files/images/collapsableClosed.gif";
	private static final String collapsableInvisibleCss = "invisible";
	private static final String collapsableOpenCss = "collapsable";
	private static final String collapsableOpenImg = "/files/images/collapsableOpen.gif";
	private File pomFile;

	private String cssClass = "collapse_rim";
	private String errorMessage;


	static {
		PageData.classpathWidgetBuilder = new WidgetBuilder(new Class[]{ClasspathWidget.class, PomWidget.class});
	}


	public PomWidget(ParentWidget parentWidget, String inputText) throws Exception {
		super(parentWidget, inputText);

		pomFile = new File(parsePomFile(inputText));
		if (FILE_UTIL.exists(pomFile)) {
			String pomParent = parsePomParentDir(inputText);
			CLASSPATH_WIDGET_FACTORY.build(this, pomParent + "classes");
			CLASSPATH_WIDGET_FACTORY.build(this, pomParent + "test-classes");
			try {
				CLASSPATH_WIDGET_FACTORY.build(this, MAVEN_DEPENDENCY_RESOLVER.resolve(pomFile));
			}
			catch (MavenException err) {
				errorMessage = err.getMessage();
			}
		}
	}


	@Override
	public String render() throws Exception {
		RawHtml titleElement = new RawHtml(HtmlUtil.metaText("&nbsp;Maven POM: " + pomFile));
		RawHtml bodyElement = new RawHtml(childHtml());
		boolean expanded = false;
		if (errorMessage != null) {
			bodyElement = new RawHtml("<pre>" + errorMessage + "</pre>");
			expanded = true;
		}
		else if (!FILE_UTIL.exists(pomFile)) {
			titleElement = new RawHtml(HtmlUtil.metaText("Maven POM could NOT be found: " + pomFile));
		}
		HtmlElement html = makeCollapsableSection(titleElement, bodyElement, expanded);
		return html.html();
	}


	private String imageSrc(boolean expanded) {
		if (expanded)
			return collapsableOpenImg;
		else
			return collapsableClosedImg;
	}


	private HtmlTag makeCollapsableDiv(boolean expanded) {
		if (expanded)
			return HtmlUtil.makeDivTag(collapsableOpenCss);
		else
			return HtmlUtil.makeDivTag(collapsableClosedCss);
	}


	private HtmlElement makeCollapsableSection(HtmlElement title, HtmlElement content, boolean expanded) {
		String id = "maven-pom";

		HtmlTag outerDiv = HtmlUtil.makeDivTag(cssClass);

		HtmlTag image = new HtmlTag("img");
		image.addAttribute("src", imageSrc(expanded));
		image.addAttribute("class", "left");
		image.addAttribute("id", "img" + id);

		HtmlTag anchor = new HtmlTag("a", image);
		anchor.addAttribute("href", "javascript:toggleCollapsable('" + id + "');");

		outerDiv.add(anchor);
		outerDiv.add(title);

		HtmlTag collapsablediv = makeCollapsableDiv(expanded);
		collapsablediv.addAttribute("id", id);
		collapsablediv.add(content);
		outerDiv.add(collapsablediv);

		return outerDiv;
	}


	private String parsePomFile(String input) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}


	private String parsePomParentDir(String input) {
		String pomFile = parsePomFile(input);
		return pomFile.substring(0, pomFile.indexOf("pom.xml"));
	}
}
