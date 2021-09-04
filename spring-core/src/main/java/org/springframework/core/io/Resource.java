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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.springframework.lang.Nullable;

/**
 * 接口的资源描述符，该描述符从实际中抽象出来
 * 底层资源的类型，例如文件或类路径资源。
 *
 *  <p> InputStream可以为每个资源打开，如果它存在
 * 物理形式，但URL或文件句柄可以直接返回
 * 某些资源。实际的行为是特定于实现的。
 * Interface for a resource descriptor that abstracts from the actual
 * type of underlying resource, such as a file or class path resource.
 *
 * <p>An InputStream can be opened for every resource if it exists in
 * physical form, but a URL or File handle can just be returned for
 * certain resources. The actual behavior is implementation-specific.
 *
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see #getInputStream()
 * @see #getURL()
 * @see #getURI()
 * @see #getFile()
 * @see WritableResource
 * @see ContextResource
 * @see UrlResource
 * @see FileUrlResource
 * @see FileSystemResource
 * @see ClassPathResource
 * @see ByteArrayResource
 * @see InputStreamResource
 */
public interface Resource extends InputStreamSource {

	/**
	 * 确定该资源是否以物理形式实际存在。
	 * * <p>该方法执行确定的存在性检查，而
	 * {@code Resource}句柄的存在只保证有效
	 * *描述符句柄。
	 * Determine whether this resource actually exists in physical form.
	 * <p>This method performs a definitive existence check, whereas the
	 * existence of a {@code Resource} handle only guarantees a valid
	 * descriptor handle.
	 */
	boolean exists();

	/**
	 * 指示此资源的非空内容是否可以通过
	 * * {@link # getInputStream()}。
	 * 对于现有的典型资源描述符，<p>将是{@code true}
	 * *，因为它严格暗示了5.1的{@link #exists()}语义。
	 * *注意，实际的内容阅读仍然可能失败，当尝试。
	 * *然而，值{@code false}是一个明确的指示
	 * 无法读取资源内容。
	 * Indicate whether non-empty contents of this resource can be read via
	 * {@link #getInputStream()}.
	 * <p>Will be {@code true} for typical resource descriptors that exist
	 * since it strictly implies {@link #exists()} semantics as of 5.1.
	 * Note that actual content reading may still fail when attempted.
	 * However, a value of {@code false} is a definitive indication
	 * that the resource content cannot be read.
	 * @see #getInputStream()
	 * @see #exists()
	 */
	default boolean isReadable() {
		return exists();
	}

	/**
	 * 指示此资源是否表示具有打开流的句柄。
	 * *如果{@code true}， InputStream不能被多次读取，
	 * 和必须被读取并关闭，以避免资源泄漏。
	 * 对于典型的资源描述符，<p>将是{@code false}。
	 * Indicate whether this resource represents a handle with an open stream.
	 * If {@code true}, the InputStream cannot be read multiple times,
	 * and must be read and closed to avoid resource leaks.
	 * <p>Will be {@code false} for typical resource descriptors.
	 */
	default boolean isOpen() {
		return false;
	}

	/**
	 * 确定此资源是否表示文件系统中的一个文件。
	 * * {@code true}的值强烈建议(但不保证)
	 * *表示{@link #getFile()}调用会成功。
	 * * <p>默认为{@code false}。
	 * Determine whether this resource represents a file in a file system.
	 * A value of {@code true} strongly suggests (but does not guarantee)
	 * that a {@link #getFile()} call will succeed.
	 * <p>This is conservatively {@code false} by default.
	 * @since 5.0
	 * @see #getFile()
	 */
	default boolean isFile() {
		return false;
	}

	/**
	 * 返回此资源的URL句柄。
	 * * @throws IOException如果资源不能解析为URL，
	 * *例如，如果资源不是可用的描述符
	 * Return a URL handle for this resource.
	 * @throws IOException if the resource cannot be resolved as URL,
	 * i.e. if the resource is not available as descriptor
	 */
	URL getURL() throws IOException;

	/**
	 * Return a URI handle for this resource.
	 * @throws IOException if the resource cannot be resolved as URI,
	 * i.e. if the resource is not available as descriptor
	 * @since 2.5
	 */
	URI getURI() throws IOException;

	/**
	 * Return a File handle for this resource.
	 * @throws java.io.FileNotFoundException if the resource cannot be resolved as
	 * absolute file path, i.e. if the resource is not available in a file system
	 * @throws IOException in case of general resolution/reading failures
	 * @see #getInputStream()
	 */
	File getFile() throws IOException;

	/**
	 * 返回一个{@link ReadableByteChannel}。
	 * * <p>预期每个调用创建一个<i>fresh</i>通道。
	 * * <p>默认实现返回{@link通道#newChannel(InputStream)}
	 * *与{@link #getInputStream()}的结果。
	 * * @return底层资源的字节通道(不能是{@code null})
	 * * @throws java.io.FileNotFoundException如果底层资源不存在
	 * * @throws IOException如果内容通道不能打开
	 * Return a {@link ReadableByteChannel}.
	 * <p>It is expected that each call creates a <i>fresh</i> channel.
	 * <p>The default implementation returns {@link Channels#newChannel(InputStream)}
	 * with the result of {@link #getInputStream()}.
	 * @return the byte channel for the underlying resource (must not be {@code null})
	 * @throws java.io.FileNotFoundException if the underlying resource doesn't exist
	 * @throws IOException if the content channel could not be opened
	 * @since 5.0
	 * @see #getInputStream()
	 */
	default ReadableByteChannel readableChannel() throws IOException {
		return Channels.newChannel(getInputStream());
	}

	/**
	 * 确定此资源的内容长度。
	 * * @throws IOException如果资源不能被解析
	 * *(在文件系统中或作为其他一些已知的物理资源类型)
	 * Determine the content length for this resource.
	 * @throws IOException if the resource cannot be resolved
	 * (in the file system or as some other known physical resource type)
	 */
	long contentLength() throws IOException;

	/**
	 * 确定此资源最后修改的时间戳。
	 * * @throws IOException如果资源不能被解析
	 * *(在文件系统中或作为其他一些已知的物理资源类型)
	 * Determine the last-modified timestamp for this resource.
	 * @throws IOException if the resource cannot be resolved
	 * (in the file system or as some other known physical resource type)
	 */
	long lastModified() throws IOException;

	/**
	 * 创建与此资源相关的资源。
	 * * @param relativePath相对路径(相对于此资源)
	 * * @return资源句柄
	 * 如果不能确定相对资源，则抛出IOException
	 * Create a resource relative to this resource.
	 * @param relativePath the relative path (relative to this resource)
	 * @return the resource handle for the relative resource
	 * @throws IOException if the relative resource cannot be determined
	 */
	Resource createRelative(String relativePath) throws IOException;

	/**
	 * 确定这个资源的文件名，通常是最后一个
	 * *部分路径:例如，"myfile.txt"。
	 * * <p>如果该类型的资源没有返回{@code null}
	 * 有一个文件名。
	 * Determine a filename for this resource, i.e. typically the last
	 * part of the path: for example, "myfile.txt".
	 * <p>Returns {@code null} if this type of resource does not
	 * have a filename.
	 */
	@Nullable
	String getFilename();

	/**
	 * 返回此资源的描述，
	 * *用于使用资源时的错误输出。
	 * * <p>也鼓励实现返回该值
	 * * from their {@code toString}方法。
	 * Return a description for this resource,
	 * to be used for error output when working with the resource.
	 * <p>Implementations are also encouraged to return this value
	 * from their {@code toString} method.
	 * @see Object#toString()
	 */
	String getDescription();

}
