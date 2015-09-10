/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goide.completion;

import com.goide.GoConstants;
import com.goide.GoParserDefinition;
import com.goide.GoTypes;
import com.goide.psi.GoFile;
import com.goide.psi.GoImportString;
import com.goide.psi.GoPackageClause;
import com.goide.psi.GoReferenceExpressionBase;
import com.goide.psi.impl.GoCachedReference;
import com.goide.psi.impl.GoPsiImplUtil;
import com.goide.runconfig.testing.GoTestFinder;
import com.goide.util.GoUtil;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GoCompletionContributor extends CompletionContributor {
  public GoCompletionContributor() {
    extend(CompletionType.BASIC, importString(), new GoImportPathsCompletionProvider());
    extend(CompletionType.BASIC, referenceExpression(), new GoReferenceCompletionProvider());
    extend(CompletionType.BASIC, goReference(), new GoReferenceCompletionProvider());
  }

  private static PsiElementPattern.Capture<PsiElement> goReference() {
    return PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement().withReference(GoCachedReference.class));
  }

  @Override
  public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
    PsiElement position = parameters.getPosition();
    PsiFile file = parameters.getOriginalFile();
    ASTNode node = position.getNode();
    if (file instanceof GoFile && position.getParent() instanceof GoPackageClause && node.getElementType() == GoTypes.IDENTIFIER) {
      boolean isTestFile = GoTestFinder.isTestFile(file);
      PsiDirectory directory = file.getParent();
      Collection<String> packagesInDirectory = GoUtil.getAllPackagesInDirectory(directory, true);
      for (String packageName : packagesInDirectory) {
        result.addElement(LookupElementBuilder.create(packageName));
        if (isTestFile) {
          result.addElement(LookupElementBuilder.create(packageName + GoConstants.TEST_SUFFIX));
        }
      }

      if (packagesInDirectory.isEmpty() && directory != null) {
        String packageFromDirectory = GoPsiImplUtil.getLocalPackageName(directory.getName());
        if (!packageFromDirectory.isEmpty()) {
          result.addElement(LookupElementBuilder.create(packageFromDirectory));
        }
      }
      result.addElement(LookupElementBuilder.create(GoConstants.MAIN));
    }
    super.fillCompletionVariants(parameters, result);
  }

  private static PsiElementPattern.Capture<PsiElement> importString() {
    return PlatformPatterns.psiElement().withElementType(GoParserDefinition.STRING_LITERALS).withParent(GoImportString.class);
  }

  private static PsiElementPattern.Capture<PsiElement> referenceExpression() {
    return PlatformPatterns.psiElement().withParent(GoReferenceExpressionBase.class);
  }
}
