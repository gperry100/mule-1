/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.config.internal;

import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.util.LazyValue;
import org.mule.runtime.config.api.dsl.model.ComponentBuildingDefinitionRegistry;
import org.mule.runtime.core.api.MuleContext;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.mule.runtime.config.internal.util.ComponentBuildingDefinitionUtils.getArtifactComponentBuildingDefinitions;
import static org.mule.runtime.config.internal.util.ComponentBuildingDefinitionUtils.getExtensionModelsComponentBuildingDefinitions;
import static org.mule.runtime.config.internal.util.ComponentBuildingDefinitionUtils.getRuntimeComponentBuildingDefinitionProvider;
import static org.mule.runtime.deployment.model.internal.application.MuleApplicationClassLoader.resolveContextArtifactPluginClassLoaders;

/**
 * Default implementation of {@link ComponentBuildingDefinitionRegistryFactory} which creates instances of
 * {@link ComponentBuildingDefinitionRegistry}
 *
 * @since 4.4.0
 */
public class DefaultComponentBuildingDefinitionRegistryProvider {
  
  private LazyValue<ComponentBuildingDefinitionRegistry> componentBuildingDefinitionRegistry = new LazyValue<>(create(null));
  
  private List<ClassLoader> pluginArtifactClassLoaders;

  public DefaultComponentBuildingDefinitionRegistryProvider(List<ClassLoader> pluginArtifactClassLoaders) {
    this.pluginArtifactClassLoaders = pluginArtifactClassLoaders;
  }
  
  public ComponentBuildingDefinitionRegistry getComponentBuildingDefinitionRegistry(MuleContext muleContext) {
    if()
    return componentBuildingDefinitionRegistry.get();
  }
  
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
