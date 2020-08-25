#!/usr/bin/env python

from converter3dji import *
import sys
import csv
import re

class GenericPsCustomizer(PsCustomizer):

	def __init__(self):
		PsCustomizer.__init__(self)

	def extractAnnot(self, pFileName):
		return True

	def extractMetadata(self, pFileName):
		return True

	def extractLinkMetadata(self, pFileName):
		return True

	def getSubPartLevel(self, pFileName):
		return 'root'

	def processConvResult(self, pDocsMap, pRootId, pSourceFilePath):

		self.handleSpecificMetadata(pDocsMap)

## MAIN
def convertor( json ):
	# Retrieve arguments
	# Command line: converter.py commandfile.json
	lParams = json
	lPsCustomizer = GenericPsCustomizer()

	with PsConverter(lParams,lPsCustomizer,FileSystemXRefResolver(lParams['rootfolder'],os.path.join(lParams['cachefolder'],'xrefs.json'))) as lConverter:
		lDefaultBuildParameters = lConverter.getDefaultBuildParameters()

		lRootId = lConverter.convert()[0]

		lDefaultBuildParameters['buildparameters']['rootstructuredocid'] = lRootId
		lDefaultBuildParameters['buildparameters']['tags'] = lParams['tag']

		lConverter.addDocument(lDefaultBuildParameters)
