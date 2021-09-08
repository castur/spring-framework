/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

/**
 * 加载资源的策略接口(例如…类路径或文件系统
 * *参考资料)。一个{@link org.springframework.context.ApplicationContext}
 * *需要提供此功能，加上扩展
 * * {@link org.springframework.core.io.support。ResourcePatternResolver}的支持。
 * ＊
 * * <p>{@link DefaultResourceLoader}是一个独立的实现
 * *可在ApplicationContext外部使用，也可由{@link ResourceEditor}使用。
 * ＊
 * * <p>资源类型的Bean属性和资源数组可以被填充
 * * from string when running in an ApplicationContext, using the
 * * context的资源加载策略。
 *
 * Strategy interface for loading resources (e.. class path or file system
 * resources). An {@link org.springframework.context.ApplicationContext}
 * is required to provide this functionality, plus extended
 * {@link org.springframework.core.io.support.ResourcePatternResolver} support.
 *
 * <p>{@link DefaultResourceLoader} is a standalone implementation that is
 * usable outside an ApplicationContext, also used by {@link ResourceEditor}.
 *
 * <p>Bean properties of type Resource and Resource array can be populated
 * from Strings when running in an ApplicationContext, using the particular
 * context's resource loading strategy.
 *
 * @author Juergen Hoeller
 * @since 10.03.2004
 * @see Resource
 * @see org.springframework.core.io.support.ResourcePatternResolver
 * @see org.springframework.context.ApplicationContext
 * @see org.springframework.context.ResourceLoaderAware
 */
public interface ResourceLoader {

	/** Pseudo URL prefix for loading from the class path: "classpath:". */
	String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;


	/**
	 * 返回指定资源位置的资源句柄。
	 * * <p>句柄应该是一个可重用的资源描述符，
	 * *允许多个{@link Resource#getInputStream()}调用。
	 * * < p > < ul >
	 * * <li>必须支持完全限定的url，例如:“文件:C: / test.dat”。
	 * * <li>必须支持类路径伪url，例如:“类路径:test.dat”。
	 * * <li>应该支持相对文件路径，例如:“web - inf / test.dat”。
	 * *(这将是特定于实现的，通常由
	 * * ApplicationContext实现。)
	 * * < / ul >
	 * * <p>注意，资源句柄并不意味着现有的资源;
	 * *你需要调用{@link Resource#exists}来检查是否存在。
	 * Return a Resource handle for the specified resource location.
	 * <p>The handle should always be a reusable resource descriptor,
	 * allowing for multiple {@link Resource#getInputStream()} calls.
	 * <p><ul>
	 * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".
	 * <li>Must support classpath pseudo-URLs, e.g. "classpath:test.dat".
	 * <li>Should support relative file paths, e.g. "WEB-INF/test.dat".
	 * (This will be implementation-specific, typically provided by an
	 * ApplicationContext implementation.)
	 * </ul>
	 * <p>Note that a Resource handle does not imply an existing resource;
	 * you need to invoke {@link Resource#exists} to check for existence.
	 * @param location the resource location
	 * @return a corresponding Resource handle (never {@code null})
	 * @see #CLASSPATH_URL_PREFIX
	 * @see Resource#exists()
	 * @see Resource#getInputStream()
	 */
	Resource getResource(String location);

	/**
	 * 公开此ResourceLoader使用的ClassLoader。
	 * * <p>需要直接访问ClassLoader的客户端可以这样做
	 * *以统一的方式与ResourceLoader，而不是依赖
	 * *在线程上下文ClassLoader上。
	 * * @return ClassLoader
	 * *(只有{@code null}，即使系统ClassLoader是不可访问的)
	 * Expose the ClassLoader used by this ResourceLoader.
	 * <p>Clients which need to access the ClassLoader directly can do so
	 * in a uniform manner with the ResourceLoader, rather than relying
	 * on the thread context ClassLoader.
	 * @return the ClassLoader
	 * (only {@code null} if even the system ClassLoader isn't accessible)
	 * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
	 * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
	 */
	@Nullable
	ClassLoader getClassLoader();

}
