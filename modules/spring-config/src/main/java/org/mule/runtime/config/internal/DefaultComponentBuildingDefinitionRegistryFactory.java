/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.config.internal;

import static java.util.Collections.emptySet;
import static org.mule.runtime.config.internal.util.ComponentBuildingDefinitionUtils.getArtifactComponentBuildingDefinitions;
import static org.mule.runtime.config.internal.util.ComponentBuildingDefinitionUtils.getExtensionModelsComponentBuildingDefinitions;
import static org.mule.runtime.config.internal.util.ComponentBuildingDefinitionUtils.getRuntimeComponentBuildingDefinitionProvider;
import static org.mule.runtime.deployment.model.internal.application.MuleApplicationClassLoader.resolveContextArtifactPluginClassLoaders;

import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.util.LazyValue;
import org.mule.runtime.config.api.dsl.model.ComponentBuildingDefinitionRegistry;
import org.mule.runtime.core.api.MuleContext;

import java.util.List;
import java.util.Set;

/**
 * Default implementation of {@link ComponentBuildingDefinitionRegistryFactory} which creates instances of
 * {@link ComponentBuildingDefinitionRegistry}
 *
 * @since 4.4.0
 */
public class DefaultComponentBuildingDefinitionRegistryFactory implements ComponentBuildingDefinitionRegistryFactory {

  private List<ClassLoader> pluginArtifactClassLoaders;

  public DefaultComponentBuildingDefinitionRegistryFactory() {}

  public DefaultComponentBuildingDefinitionRegistryFactory(List<ClassLoader> pluginArtifactClassLoaders) {
    this.pluginArtifactClassLoaders = pluginArtifactClassLoaders;
  }

  public static LazyValue<ComponentBuildingDefinitionRegistry> createLazy(MuleContext muleContext) {
    return new LazyValue(() -> createComponentBuildingDefinitionRegistry(muleContext));
  }
  
  public static LazyValue<ComponentBuildingDefinitionRegistry> createLazy(MuleContext muleContext, List<ClassLoader> pluginArtifactClassLoaders) {
    return new LazyValue(() -> createComponentBuildingDefinitionRegistry(muleContext, pluginArtifactClassLoaders));
  }

  private static ComponentBuildingDefinitionRegistry createComponentBuildingDefinitionRegistry(MuleContext muleContext) {
    return createComponentBuildingDefinitionRegistry(muleContext, null);
  }

  private static ComponentBuildingDefinitionRegistry createComponentBuildingDefinitionRegistry(MuleContext muleContext, List<ClassLoader> pluginArtifactClassLoaders) {
    ComponentBuildingDefinitionRegistry componentBuildingDefinitionRegistry = new ComponentBuildingDefinitionRegistry();

    getRuntimeComponentBuildingDefinitionProvider().getComponentBuildingDefinitions()
            .forEach(componentBuildingDefinitionRegistry::register);

    Set<ExtensionModel> extensions =
            muleContext.getExtensionManager() == null ? emptySet() : muleContext.getExtensionManager().getExtensions();

    getExtensionModelsComponentBuildingDefinitions(extensions, DslResolvingContext.getDefault(extensions))
            .forEach(componentBuildingDefinitionRegistry::register);

    List<ClassLoader> classLoaders =
            pluginArtifactClassLoaders == null ? resolveContextArtifactPluginClassLoaders() : pluginArtifactClassLoaders;

    for (ClassLoader pluginArtifactClassLoader : classLoaders) {
      getArtifactComponentBuildingDefinitions(pluginArtifactClassLoader)
              .forEach(componentBuildingDefinitionRegistry::register);
    }
    return componentBuildingDefinitionRegistry;
  }

  @Override
  public ComponentBuildingDefinitionRegistry create(MuleContext muleContext) {
    ComponentBuildingDefinitionRegistry componentBuildingDefinitionRegistry = new ComponentBuildingDefinitionRegistry();

    getRuntimeComponentBuildingDefinitionProvider().getComponentBuildingDefinitions()
        .forEach(componentBuildingDefinitionRegistry::register);

    Set<ExtensionModel> extensions =
        muleContext.getExtensionManager() == null ? emptySet() : muleContext.getExtensionManager().getExtensions();

    getExtensionModelsComponentBuildingDefinitions(extensions, DslResolvingContext.getDefault(extensions))
        .forEach(componentBuildingDefinitionRegistry::register);

    List<ClassLoader> classLoaders =
        pluginArtifactClassLoaders == null ? resolveContextArtifactPluginClassLoaders() : pluginArtifactClassLoaders;

    for (ClassLoader pluginArtifactClassLoader : classLoaders) {
      getArtifactComponentBuildingDefinitions(pluginArtifactClassLoader)
          .forEach(componentBuildingDefinitionRegistry::register);
    }
    return componentBuildingDefinitionRegistry;
  }
}
