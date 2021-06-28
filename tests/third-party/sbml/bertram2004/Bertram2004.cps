<?xml version="1.0" encoding="UTF-8"?>
<!-- generated with COPASI 4.26 (Build 213) (http://www.copasi.org) at 2020-12-11T09:57:07Z -->
<?oxygen RNGSchema="http://www.copasi.org/static/schema/CopasiML.rng" type="xml"?>
<COPASI xmlns="http://www.copasi.org/static/schema" versionMajor="4" versionMinor="26" versionDevel="213" copasiSourcesModified="0">
  <Model key="Model_0" name="Bertram2004_PancreaticBetaCell_modelB" simulationType="time" timeUnit="s" volumeUnit="l" areaUnit="m²" lengthUnit="m" quantityUnit="mol" type="deterministic" avogadroConstant="6.0221408570000002e+23">
    <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:bqbiol="http://biomodels.net/biology-qualifiers/"
   xmlns:bqmodel="http://biomodels.net/model-qualifiers/"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   xmlns:vCard="http://www.w3.org/2001/vcard-rdf/3.0#">
  <rdf:Description rdf:about="#Model_0">
    <bqbiol:hasProperty>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/doid/DOID:9351"/>
      </rdf:Bag>
    </bqbiol:hasProperty>
    <bqbiol:hasTaxon>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/taxonomy/9606"/>
      </rdf:Bag>
    </bqbiol:hasTaxon>
    <bqmodel:isDerivedFrom>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/pubmed/15294427"/>
      </rdf:Bag>
    </bqmodel:isDerivedFrom>
    <dcterms:bibliographicCitation>
      <rdf:Bag>
        <rdf:li>
          <rdf:Description>
            <CopasiMT:isDescribedBy rdf:resource="http://identifiers.org/pubmed/15347584"/>
          </rdf:Description>
        </rdf:li>
      </rdf:Bag>
    </dcterms:bibliographicCitation>
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2011-09-29T22:04:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <dcterms:creator>
      <rdf:Bag>
        <rdf:li>
          <rdf:Description>
            <vCard:EMAIL>ajmera@ebi.ac.uk</vCard:EMAIL>
            <vCard:N>
              <rdf:Description>
                <vCard:Family>Ajmera</vCard:Family>
                <vCard:Given>Ishan</vCard:Given>
              </rdf:Description>
            </vCard:N>
            <vCard:ORG>
              <rdf:Description>
                <vCard:Orgname>EMBL-EBI</vCard:Orgname>
              </rdf:Description>
            </vCard:ORG>
          </rdf:Description>
        </rdf:li>
        <rdf:li>
          <rdf:Description>
            <vCard:EMAIL>c.lloyd@aukland.ac.nz</vCard:EMAIL>
            <vCard:N>
              <rdf:Description>
                <vCard:Family>Lloyd</vCard:Family>
                <vCard:Given>Catherine</vCard:Given>
              </rdf:Description>
            </vCard:N>
            <vCard:ORG>
              <rdf:Description>
                <vCard:Orgname>The University of Auckland,The Bioengineering Institute</vCard:Orgname>
              </rdf:Description>
            </vCard:ORG>
          </rdf:Description>
        </rdf:li>
      </rdf:Bag>
    </dcterms:creator>
    <dcterms:modified>
      <rdf:Description>
        <dcterms:W3CDTF>2016-04-08T17:06:30Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:modified>
    <CopasiMT:is>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/biomodels.db/MODEL1006230042"/>
      </rdf:Bag>
    </CopasiMT:is>
    <CopasiMT:is>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/biomodels.db/BIOMD0000000373"/>
      </rdf:Bag>
    </CopasiMT:is>
    <CopasiMT:isVersionOf>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/go/GO:0006096"/>
        <rdf:li rdf:resource="http://identifiers.org/go/GO:0044342"/>
        <rdf:li rdf:resource="http://identifiers.org/go/GO:0061469"/>
      </rdf:Bag>
    </CopasiMT:isVersionOf>
    <CopasiMT:occursIn>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/bto/BTO:0000783"/>
      </rdf:Bag>
    </CopasiMT:occursIn>
  </rdf:Description>
</rdf:RDF>

    </MiriamAnnotation>
    <Comment>
      
  <body xmlns="http://www.w3.org/1999/xhtml">
    <p>
      This a model from the article:
      <br/>
    <strong> Calcium and glycolysis mediate multiple bursting modes in pancreatic islets.
</strong>
    <br/>
Bertram R, Satin L, Zhang M, Smolen P, Sherman A.
      <em>Biophys J</em>2004 Nov;87(5):3074-87 
      <a href="http://www.ncbi.nlm.nih.gov/pubmed/15347584">15347584</a>,
      <br/>
    <strong>Abstract:</strong>
    <br/>
Pancreatic islets of Langerhans produce bursts of electrical activity when
exposed to stimulatory glucose levels. These bursts often have a regular
repeating pattern, with a period of 10-60 s. In some cases, however, the bursts
are episodic, clustered into bursts of bursts, which we call compound bursting.
Consistent with this are recordings of free Ca2+ concentration, oxygen
consumption, mitochondrial membrane potential, and intraislet glucose levels
that exhibit very slow oscillations, with faster oscillations superimposed. We
describe a new mathematical model of the pancreatic beta-cell that can account
for these multimodal patterns. The model includes the feedback of cytosolic Ca2+
onto ion channels that can account for bursting, and a metabolic subsystem that
is capable of producing slow oscillations driven by oscillations in glycolysis.
This slow rhythm is responsible for the slow mode of compound bursting in the
model. We also show that it is possible for glycolytic oscillations alone to
drive a very slow form of bursting, which we call &quot;glycolytic bursting.&quot;
Finally, the model predicts that there is bistability between stationary and
oscillatory glycolysis for a range of parameter values. We provide experimental
support for this model prediction. Overall, the model can account for a
diversity of islet behaviors described in the literature over the past 20 years.
   </p>
    <p>
This model was taken from the
      <a href="http://www.cellml.org/models">CellML repository</a> and automatically converted to SBML.
      <br/>
The original model was:
<a href="http://models.cellml.org/exposure/f7d6dbef9db48b6d62bf43598ebfb2d5">
      <strong>Bertram R, Satin L, Zhang M, Smolen P, Sherman A. (2004) - version=1.0</strong>
    </a>
    <br/>
The original CellML model was created by:
      <br/>
    <strong>Catherine Lloyd</strong>
    <br/>
c.lloyd@auckland.ac.nz
      <br/>
The University of Auckland
      <br/></p>
    <p>This model originates from BioModels Database: A Database of Annotated Published Models (http://www.ebi.ac.uk/biomodels/). It is copyright (c) 2005-2011 The BioModels.net Team.<br/>
For more information see the <a href="http://www.ebi.ac.uk/biomodels/legal.html" target="_blank">terms of use</a>.<br/>
To cite BioModels Database, please use: <a href="http://www.ncbi.nlm.nih.gov/pubmed/20587024" target="_blank">Li C, Donizelli M, Rodriguez N, Dharuri H, Endler L, Chelliah V, Li L, He E, Henry A, Stefan MI, Snoep JL, Hucka M, Le Novère N, Laibe C (2010) BioModels Database: An enhanced, curated and annotated resource for published quantitative kinetic models. BMC Syst Biol., 4:92.</a></p>
  </body>

    </Comment>
    <ListOfCompartments>
      <Compartment key="Compartment_3" name="COMpartment" simulationType="fixed" dimensionality="3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Compartment_3">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Compartment>
    </ListOfCompartments>
    <ListOfMetabolites>
      <Metabolite key="Metabolite_16" name="V" simulationType="ode" compartment="Compartment_3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_16">
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000259"/>
    <CopasiMT:is>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/pato/PATO:0001462"/>
      </rdf:Bag>
    </CopasiMT:is>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Expression>
          -(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[IK],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ICa],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[IKCa],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[IKATP],Reference=Value>)/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Cm],Reference=Value>
        </Expression>
      </Metabolite>
      <Metabolite key="Metabolite_15" name="n" simulationType="ode" compartment="Compartment_3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_15">
    <CopasiMT:isVersionOf>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/go/GO:0005251"/>
      </rdf:Bag>
    </CopasiMT:isVersionOf>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Expression>
          (&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ninf],Reference=Value>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[n],Reference=Concentration>)/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[taun],Reference=Value>
        </Expression>
      </Metabolite>
      <Metabolite key="Metabolite_14" name="c" simulationType="ode" compartment="Compartment_3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_14">
    <CopasiMT:isVersionOf>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/chebi/CHEBI:29108"/>
      </rdf:Bag>
    </CopasiMT:isVersionOf>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fcyt],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jmem],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jer],Reference=Value>)
        </Expression>
      </Metabolite>
      <Metabolite key="Metabolite_13" name="cer" simulationType="ode" compartment="Compartment_3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_13">
    <CopasiMT:isVersionOf>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/chebi/CHEBI:29108"/>
      </rdf:Bag>
    </CopasiMT:isVersionOf>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Expression>
          -&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fer],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[sigmaV],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jer],Reference=Value>
        </Expression>
      </Metabolite>
      <Metabolite key="Metabolite_12" name="g6p" simulationType="ode" compartment="Compartment_3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_12">
    <CopasiMT:isVersionOf>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/chebi/CHEBI:14314"/>
      </rdf:Bag>
    </CopasiMT:isVersionOf>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[lambda],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Rgk],Reference=Value>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[pfk],Reference=Value>)
        </Expression>
      </Metabolite>
      <Metabolite key="Metabolite_11" name="fbp" simulationType="ode" compartment="Compartment_3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_11">
    <CopasiMT:isVersionOf>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/chebi/CHEBI:16905"/>
      </rdf:Bag>
    </CopasiMT:isVersionOf>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[lambda],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[pfk],Reference=Value>/1-0.5*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[rgpdh],Reference=Value>)
        </Expression>
      </Metabolite>
      <Metabolite key="Metabolite_10" name="adp" simulationType="ode" compartment="Compartment_3" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_10">
    <CopasiMT:isVersionOf>
      <rdf:Bag>
        <rdf:li rdf:resource="http://identifiers.org/chebi/CHEBI:16761"/>
      </rdf:Bag>
    </CopasiMT:isVersionOf>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Expression>
          (&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>*exp(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fback],Reference=Value>*(1-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[c],Reference=Concentration>/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[r1],Reference=Value>)))/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[taua],Reference=Value>*1)
        </Expression>
      </Metabolite>
    </ListOfMetabolites>
    <ListOfModelValues>
      <ModelValue key="ModelValue_263" name="IK" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_263">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gK],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[n],Reference=Concentration>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[V],Reference=Concentration>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[VK],Reference=Value>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_264" name="ICa" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_264">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gCa],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[minf],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[V],Reference=Concentration>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[VCa],Reference=Value>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_265" name="IKCa" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_265">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gKCa],Reference=Value>/(1+(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kd],Reference=Value>/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[c],Reference=Concentration>)^2)*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[V],Reference=Concentration>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[VK],Reference=Value>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_266" name="Cm" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_266">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000258"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_267" name="gK" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_267">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_268" name="gKCa" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_268">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_269" name="kd" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_269">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_270" name="gCa" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_270">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_271" name="minf" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_271">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          1/(1+exp(-(20+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[V],Reference=Concentration>/1)/12))
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_272" name="VCa" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_272">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_273" name="taun" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_273">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_274" name="ninf" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_274">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          1/(1+exp(-(16+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[V],Reference=Concentration>/1)/5))
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_275" name="fcyt" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_275">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_276" name="Jmem" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_276">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          -(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[alpha],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ICa],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kpmca],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[c],Reference=Concentration>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_277" name="Jer" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_277">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[epser],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jleak],Reference=Value>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jserca],Reference=Value>)/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[lambdaer],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_278" name="fer" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_278">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_279" name="sigmaV" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_279">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_280" name="pleak" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_280">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_281" name="Kserca" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_281">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_282" name="lambdaer" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_282">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_283" name="epser" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_283">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_284" name="alpha" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_284">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_182" name="kpmca" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_182">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_181" name="Jserca" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_181">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Kserca],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[c],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_180" name="Jleak" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_180">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[pleak],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[cer],Reference=Concentration>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[c],Reference=Concentration>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_179" name="rgpdh" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_179">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.20000000000000001*abs(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>*1/1^2)^(1/2)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_178" name="Rgk" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_178">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_177" name="atot" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_177">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_176" name="pfkbas" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_176">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_175" name="f6p" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_175">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.29999999999999999*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[g6p],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_174" name="lambda" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_174">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_173" name="pfk" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_173">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          1*((&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[pfkbas],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[cat],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa16],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[cat],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topb],Reference=Value>)/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom16],Reference=Value>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_172" name="bottom1" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_172">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_171" name="topa1" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_171">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_170" name="k1" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_170">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_169" name="k2" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_169">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_168" name="k3" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_168">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_167" name="k4" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_167">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_166" name="cat" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_166">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_165" name="weight2" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_165">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_164" name="topa2" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_164">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa1],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_163" name="bottom2" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_163">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom1],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight2],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_162" name="topa3" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_162">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa2],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight3],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_161" name="weight3" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_161">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_160" name="bottom3" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_160">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom2],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight3],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_159" name="famp" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_159">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_158" name="fatp" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_158">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_157" name="ffbp" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_157">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_156" name="fbt" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_156">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_155" name="fmt" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_155">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_154" name="weight4" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_154">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          (&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>)^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fatp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*1^2)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_153" name="topa4" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_153">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa3],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight4],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_152" name="bottom4" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_152">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom3],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight4],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_151" name="weight5" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_151">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_150" name="topa5" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_150">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa4],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_149" name="bottom5" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_149">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom4],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight5],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_148" name="weight6" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_148">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fbt],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_147" name="topa6" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_147">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa5],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_146" name="bottom6" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_146">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom5],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight6],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_145" name="weight7" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_145">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ffbp],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_144" name="topa7" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_144">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa6],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight7],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_143" name="bottom7" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_143">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom6],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight7],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_142" name="weight8" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_142">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>^2*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ffbp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fbt],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fatp],Reference=Value>*1^2)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_141" name="topa8" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_141">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa7],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight8],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_140" name="bottom8" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_140">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom7],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight8],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_139" name="weight9" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_139">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_138" name="topa9" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_138">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa8],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_137" name="bottom9" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_137">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom8],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight9],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_136" name="weight10" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_136">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fmt],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_135" name="topa10" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_135">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa9],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_134" name="bottom10" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_134">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom9],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight10],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_133" name="weight11" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_133">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[famp],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_132" name="topa11" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_132">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa10],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight11],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_131" name="bottom11" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_131">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom10],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight11],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_130" name="weight12" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_130">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>^2*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[famp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fmt],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fatp],Reference=Value>*1^2)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_129" name="topa12" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_129">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa11],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight12],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_128" name="bottom12" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_128">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom11],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight12],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_127" name="weight13" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_127">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_126" name="topa13" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_126">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa12],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_125" name="bottom13" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_125">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom12],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight13],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_124" name="weight14" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_124">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fbt],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fmt],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_123" name="topa14" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_123">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa13],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_122" name="bottom14" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_122">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom13],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight14],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_121" name="weight15" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_121">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ffbp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[famp],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_120" name="topa15" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_120">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa14],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_119" name="bottom15" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_119">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom14],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight15],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_118" name="weight16" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_118">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp],Reference=Concentration>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p],Reference=Value>^2*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>^2/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ffbp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[famp],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fbt],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fmt],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fatp],Reference=Value>*1^2)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_117" name="topa16" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_117">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa15],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight16],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_116" name="bottom16" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_116">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom15],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight16],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_115" name="topb" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_115">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight15],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_114" name="mgadp" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_114">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.16500000000000001*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_113" name="adp3m" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_113">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.13500000000000001*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_112" name="atp4m" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_112">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.050000000000000003*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_111" name="topo" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_111">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.080000000000000002*(1+2*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[mgadp],Reference=Value>/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kdd],Reference=Value>*1))+0.89000000000000001*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[mgadp],Reference=Value>/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kdd],Reference=Value>*1))^2
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_110" name="bottomo" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_110">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          (1+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[mgadp],Reference=Value>/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kdd],Reference=Value>*1))^2*(1+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[adp3m],Reference=Value>/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ktd],Reference=Value>*1)+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp4m],Reference=Value>/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ktt],Reference=Value>*1))
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_109" name="katpo" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_109">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topo],Reference=Value>/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottomo],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_108" name="IKATP" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_108">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gkatpbar],Reference=Value>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[katpo],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[V],Reference=Concentration>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[VK],Reference=Value>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_107" name="VK" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_107">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_106" name="gkatpbar" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_106">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_105" name="kdd" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_105">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_104" name="ktd" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_104">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_103" name="ktt" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_103">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_102" name="atp" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_102">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.5*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atot],Reference=Value>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[rad],Reference=Value>*1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_101" name="fback" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_101">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[r],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[y],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_100" name="taua" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_100">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_99" name="r1" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_99">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_98" name="r" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_98">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_97" name="y" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_97">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[vg],Reference=Value>*(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[rgpdh],Reference=Value>/(&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kg],Reference=Value>+&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[rgpdh],Reference=Value>))
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_96" name="vg" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_96">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_95" name="kg" simulationType="fixed" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_95">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000009"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_94" name="amp" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_94">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_93" name="rad" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_93">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          abs((&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>-&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atot],Reference=Value>)^2-4*&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>^2)^(1/2)/1
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_92" name="ratio" simulationType="assignment" addNoise="false">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelValue_92">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp],Reference=Value>/&lt;CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp],Reference=Concentration>
        </Expression>
      </ModelValue>
    </ListOfModelValues>
    <ListOfModelParameterSets activeSet="ModelParameterSet_0">
      <ModelParameterSet key="ModelParameterSet_0" name="Initial State">
        <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#ModelParameterSet_0">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:57:00Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment]" value="1" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[V]" value="-3.6132845142000003e+25" type="Species" simulationType="ode"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[n]" value="1.5055352142500001e+22" type="Species" simulationType="ode"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[c]" value="1.50553521425e+23" type="Species" simulationType="ode"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[cer]" value="1.114096058545e+26" type="Species" simulationType="ode"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[g6p]" value="1.2044281713999999e+26" type="Species" simulationType="ode"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[fbp]" value="2.4088563427999999e+25" type="Species" simulationType="ode"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Compartments[COMpartment],Vector=Metabolites[adp]" value="4.6972698684599998e+26" type="Species" simulationType="ode"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[IK]" value="1012.5" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ICa]" value="-2927.8416316279495" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[IKCa]" value="1800" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Cm]" value="5300" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gK]" value="2700" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gKCa]" value="600" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kd]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gCa]" value="1000" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[minf]" value="0.034445195666211167" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[VCa]" value="25" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[taun]" value="20" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ninf]" value="0.00015071035805975741" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fcyt]" value="0.01" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jmem]" value="-0.03682471265767423" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jer]" value="-0.063049999999999995" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fer]" value="0.01" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[sigmaV]" value="31" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[pleak]" value="0.00020000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Kserca]" value="0.40000000000000002" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[lambdaer]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[epser]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[alpha]" value="4.5000000000000001e-06" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kpmca]" value="0.20000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jserca]" value="0.10000000000000001" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Jleak]" value="0.036950000000000004" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[rgpdh]" value="1.264911064067352" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[Rgk]" value="0.20000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atot]" value="3000" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[pfkbas]" value="0.059999999999999998" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[f6p]" value="60" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[lambda]" value="0.0050000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[pfk]" value="0.55082928813139509" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom1]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa1]" value="0" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k1]" value="30" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k2]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k3]" value="50000" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[k4]" value="1000" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[cat]" value="2" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight2]" value="3609.0378846009467" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa2]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom2]" value="3610.0378846009467" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa3]" value="0.071999999999999995" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight3]" value="0.071999999999999995" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom3]" value="3610.1098846009468" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[famp]" value="0.02" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fatp]" value="20" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ffbp]" value="0.20000000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fbt]" value="20" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fmt]" value="20" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight4]" value="12.992536384563406" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa4]" value="13.064536384563405" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom4]" value="3623.10242098551" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight5]" value="40" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa5]" value="13.064536384563405" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom5]" value="3663.10242098551" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight6]" value="7218.0757692018924" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa6]" value="13.064536384563405" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom6]" value="10881.178190187402" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight7]" value="14.4" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa7]" value="27.464536384563406" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom7]" value="10895.578190187402" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight8]" value="129.92536384563408" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa8]" value="157.3899002301975" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom8]" value="11025.503554033035" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight9]" value="10.675106837823632" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa9]" value="157.3899002301975" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom9]" value="11036.178660870859" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight10]" value="1926.3432499934054" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa10]" value="157.3899002301975" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom10]" value="12962.521910864265" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight11]" value="38.430384616165078" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa11]" value="195.82028484636257" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom11]" value="13000.952295480431" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight12]" value="346.74178499881293" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa12]" value="542.56206984517553" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom12]" value="13347.694080479243" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight13]" value="427.0042735129453" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa13]" value="542.56206984517553" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom13]" value="13774.698353992188" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight14]" value="3852.6864999868098" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa14]" value="542.56206984517553" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom14]" value="17627.384853978998" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight15]" value="7686.0769232330149" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa15]" value="542.56206984517553" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom15]" value="25313.461777212011" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[weight16]" value="3467.4178499881291" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topa16]" value="4009.9799198333048" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottom16]" value="28780.879627200142" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topb]" value="7686.0769232330149" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[mgadp]" value="128.70000000000002" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[adp3m]" value="105.30000000000001" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp4m]" value="94.987339743264556" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[topo]" value="52.30058166089966" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[bottomo]" value="7348.2410600916746" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[katpo]" value="0.0071174286789452675" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[IKATP]" value="2669.0357546044752" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[VK]" value="-75" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[gkatpbar]" value="25000" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kdd]" value="17" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ktd]" value="26" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ktt]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[atp]" value="1899.7467948652911" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[fback]" value="1.247032961478473" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[taua]" value="300000" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[r1]" value="0.34999999999999998" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[r]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[y]" value="0.24703296147847301" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[vg]" value="2.2000000000000002" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[kg]" value="10" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[amp]" value="320.25320513470899" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[rad]" value="1579.493589730582" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Bertram2004_PancreaticBetaCell_modelB,Vector=Values[ratio]" value="2.4355728139298605" type="ModelValue" simulationType="assignment"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
        </ModelParameterGroup>
      </ModelParameterSet>
    </ListOfModelParameterSets>
    <StateTemplate>
      <StateTemplateVariable objectReference="Model_0"/>
      <StateTemplateVariable objectReference="Metabolite_16"/>
      <StateTemplateVariable objectReference="Metabolite_15"/>
      <StateTemplateVariable objectReference="Metabolite_14"/>
      <StateTemplateVariable objectReference="Metabolite_13"/>
      <StateTemplateVariable objectReference="Metabolite_12"/>
      <StateTemplateVariable objectReference="Metabolite_11"/>
      <StateTemplateVariable objectReference="Metabolite_10"/>
      <StateTemplateVariable objectReference="ModelValue_263"/>
      <StateTemplateVariable objectReference="ModelValue_264"/>
      <StateTemplateVariable objectReference="ModelValue_265"/>
      <StateTemplateVariable objectReference="ModelValue_271"/>
      <StateTemplateVariable objectReference="ModelValue_274"/>
      <StateTemplateVariable objectReference="ModelValue_276"/>
      <StateTemplateVariable objectReference="ModelValue_277"/>
      <StateTemplateVariable objectReference="ModelValue_181"/>
      <StateTemplateVariable objectReference="ModelValue_180"/>
      <StateTemplateVariable objectReference="ModelValue_179"/>
      <StateTemplateVariable objectReference="ModelValue_175"/>
      <StateTemplateVariable objectReference="ModelValue_173"/>
      <StateTemplateVariable objectReference="ModelValue_165"/>
      <StateTemplateVariable objectReference="ModelValue_164"/>
      <StateTemplateVariable objectReference="ModelValue_163"/>
      <StateTemplateVariable objectReference="ModelValue_162"/>
      <StateTemplateVariable objectReference="ModelValue_161"/>
      <StateTemplateVariable objectReference="ModelValue_160"/>
      <StateTemplateVariable objectReference="ModelValue_154"/>
      <StateTemplateVariable objectReference="ModelValue_153"/>
      <StateTemplateVariable objectReference="ModelValue_152"/>
      <StateTemplateVariable objectReference="ModelValue_151"/>
      <StateTemplateVariable objectReference="ModelValue_150"/>
      <StateTemplateVariable objectReference="ModelValue_149"/>
      <StateTemplateVariable objectReference="ModelValue_148"/>
      <StateTemplateVariable objectReference="ModelValue_147"/>
      <StateTemplateVariable objectReference="ModelValue_146"/>
      <StateTemplateVariable objectReference="ModelValue_145"/>
      <StateTemplateVariable objectReference="ModelValue_144"/>
      <StateTemplateVariable objectReference="ModelValue_143"/>
      <StateTemplateVariable objectReference="ModelValue_142"/>
      <StateTemplateVariable objectReference="ModelValue_141"/>
      <StateTemplateVariable objectReference="ModelValue_140"/>
      <StateTemplateVariable objectReference="ModelValue_139"/>
      <StateTemplateVariable objectReference="ModelValue_138"/>
      <StateTemplateVariable objectReference="ModelValue_137"/>
      <StateTemplateVariable objectReference="ModelValue_136"/>
      <StateTemplateVariable objectReference="ModelValue_135"/>
      <StateTemplateVariable objectReference="ModelValue_134"/>
      <StateTemplateVariable objectReference="ModelValue_133"/>
      <StateTemplateVariable objectReference="ModelValue_132"/>
      <StateTemplateVariable objectReference="ModelValue_131"/>
      <StateTemplateVariable objectReference="ModelValue_130"/>
      <StateTemplateVariable objectReference="ModelValue_129"/>
      <StateTemplateVariable objectReference="ModelValue_128"/>
      <StateTemplateVariable objectReference="ModelValue_127"/>
      <StateTemplateVariable objectReference="ModelValue_126"/>
      <StateTemplateVariable objectReference="ModelValue_125"/>
      <StateTemplateVariable objectReference="ModelValue_124"/>
      <StateTemplateVariable objectReference="ModelValue_123"/>
      <StateTemplateVariable objectReference="ModelValue_122"/>
      <StateTemplateVariable objectReference="ModelValue_121"/>
      <StateTemplateVariable objectReference="ModelValue_120"/>
      <StateTemplateVariable objectReference="ModelValue_119"/>
      <StateTemplateVariable objectReference="ModelValue_118"/>
      <StateTemplateVariable objectReference="ModelValue_117"/>
      <StateTemplateVariable objectReference="ModelValue_116"/>
      <StateTemplateVariable objectReference="ModelValue_115"/>
      <StateTemplateVariable objectReference="ModelValue_114"/>
      <StateTemplateVariable objectReference="ModelValue_113"/>
      <StateTemplateVariable objectReference="ModelValue_112"/>
      <StateTemplateVariable objectReference="ModelValue_111"/>
      <StateTemplateVariable objectReference="ModelValue_110"/>
      <StateTemplateVariable objectReference="ModelValue_109"/>
      <StateTemplateVariable objectReference="ModelValue_108"/>
      <StateTemplateVariable objectReference="ModelValue_102"/>
      <StateTemplateVariable objectReference="ModelValue_101"/>
      <StateTemplateVariable objectReference="ModelValue_97"/>
      <StateTemplateVariable objectReference="ModelValue_94"/>
      <StateTemplateVariable objectReference="ModelValue_93"/>
      <StateTemplateVariable objectReference="ModelValue_92"/>
      <StateTemplateVariable objectReference="Compartment_3"/>
      <StateTemplateVariable objectReference="ModelValue_266"/>
      <StateTemplateVariable objectReference="ModelValue_267"/>
      <StateTemplateVariable objectReference="ModelValue_268"/>
      <StateTemplateVariable objectReference="ModelValue_269"/>
      <StateTemplateVariable objectReference="ModelValue_270"/>
      <StateTemplateVariable objectReference="ModelValue_272"/>
      <StateTemplateVariable objectReference="ModelValue_273"/>
      <StateTemplateVariable objectReference="ModelValue_275"/>
      <StateTemplateVariable objectReference="ModelValue_278"/>
      <StateTemplateVariable objectReference="ModelValue_279"/>
      <StateTemplateVariable objectReference="ModelValue_280"/>
      <StateTemplateVariable objectReference="ModelValue_281"/>
      <StateTemplateVariable objectReference="ModelValue_282"/>
      <StateTemplateVariable objectReference="ModelValue_283"/>
      <StateTemplateVariable objectReference="ModelValue_284"/>
      <StateTemplateVariable objectReference="ModelValue_182"/>
      <StateTemplateVariable objectReference="ModelValue_178"/>
      <StateTemplateVariable objectReference="ModelValue_177"/>
      <StateTemplateVariable objectReference="ModelValue_176"/>
      <StateTemplateVariable objectReference="ModelValue_174"/>
      <StateTemplateVariable objectReference="ModelValue_172"/>
      <StateTemplateVariable objectReference="ModelValue_171"/>
      <StateTemplateVariable objectReference="ModelValue_170"/>
      <StateTemplateVariable objectReference="ModelValue_169"/>
      <StateTemplateVariable objectReference="ModelValue_168"/>
      <StateTemplateVariable objectReference="ModelValue_167"/>
      <StateTemplateVariable objectReference="ModelValue_166"/>
      <StateTemplateVariable objectReference="ModelValue_159"/>
      <StateTemplateVariable objectReference="ModelValue_158"/>
      <StateTemplateVariable objectReference="ModelValue_157"/>
      <StateTemplateVariable objectReference="ModelValue_156"/>
      <StateTemplateVariable objectReference="ModelValue_155"/>
      <StateTemplateVariable objectReference="ModelValue_107"/>
      <StateTemplateVariable objectReference="ModelValue_106"/>
      <StateTemplateVariable objectReference="ModelValue_105"/>
      <StateTemplateVariable objectReference="ModelValue_104"/>
      <StateTemplateVariable objectReference="ModelValue_103"/>
      <StateTemplateVariable objectReference="ModelValue_100"/>
      <StateTemplateVariable objectReference="ModelValue_99"/>
      <StateTemplateVariable objectReference="ModelValue_98"/>
      <StateTemplateVariable objectReference="ModelValue_96"/>
      <StateTemplateVariable objectReference="ModelValue_95"/>
    </StateTemplate>
    <InitialState type="initialState">
      0 -3.6132845142000003e+25 1.5055352142500001e+22 1.50553521425e+23 1.114096058545e+26 1.2044281713999999e+26 2.4088563427999999e+25 4.6972698684599998e+26 1012.5 -2927.8416316279495 1800 0.034445195666211167 0.00015071035805975741 -0.03682471265767423 -0.063049999999999995 0.10000000000000001 0.036950000000000004 1.264911064067352 60 0.55082928813139509 3609.0378846009467 0 3610.0378846009467 0.071999999999999995 0.071999999999999995 3610.1098846009468 12.992536384563406 13.064536384563405 3623.10242098551 40 13.064536384563405 3663.10242098551 7218.0757692018924 13.064536384563405 10881.178190187402 14.4 27.464536384563406 10895.578190187402 129.92536384563408 157.3899002301975 11025.503554033035 10.675106837823632 157.3899002301975 11036.178660870859 1926.3432499934054 157.3899002301975 12962.521910864265 38.430384616165078 195.82028484636257 13000.952295480431 346.74178499881293 542.56206984517553 13347.694080479243 427.0042735129453 542.56206984517553 13774.698353992188 3852.6864999868098 542.56206984517553 17627.384853978998 7686.0769232330149 542.56206984517553 25313.461777212011 3467.4178499881291 4009.9799198333048 28780.879627200142 7686.0769232330149 128.70000000000002 105.30000000000001 94.987339743264556 52.30058166089966 7348.2410600916746 0.0071174286789452675 2669.0357546044752 1899.7467948652911 1.247032961478473 0.24703296147847301 320.25320513470899 1579.493589730582 2.4355728139298605 1 5300 2700 600 0.5 1000 25 20 0.01 0.01 31 0.00020000000000000001 0.40000000000000002 1 1 4.5000000000000001e-06 0.20000000000000001 0.20000000000000001 3000 0.059999999999999998 0.0050000000000000001 1 0 30 1 50000 1000 2 0.02 20 0.20000000000000001 20 20 -75 25000 17 26 1 300000 0.34999999999999998 1 2.2000000000000002 10 
    </InitialState>
  </Model>
  <ListOfTasks>
    <Task key="Task_13" name="Steady-State" type="steadyState" scheduled="false" updateModel="false">
      <Report reference="Report_0" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="JacobianRequested" type="bool" value="1"/>
        <Parameter name="StabilityAnalysisRequested" type="bool" value="1"/>
      </Problem>
      <Method name="Enhanced Newton" type="EnhancedNewton">
        <Parameter name="Resolution" type="unsignedFloat" value="1.0000000000000001e-09"/>
        <Parameter name="Derivation Factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Use Newton" type="bool" value="1"/>
        <Parameter name="Use Integration" type="bool" value="1"/>
        <Parameter name="Use Back Integration" type="bool" value="0"/>
        <Parameter name="Accept Negative Concentrations" type="bool" value="0"/>
        <Parameter name="Iteration Limit" type="unsignedInteger" value="50"/>
        <Parameter name="Maximum duration for forward integration" type="unsignedFloat" value="1000000000"/>
        <Parameter name="Maximum duration for backward integration" type="unsignedFloat" value="1000000"/>
      </Method>
    </Task>
    <Task key="Task_1" name="Time-Course" type="timeCourse" scheduled="true" updateModel="false">
	  <Report reference="Report_10" target="report.txt" append="1" confirmOverwrite="1"/>
	    <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="0"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="100000"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1000"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="Use Values" type="bool" value="0"/>
        <Parameter name="Values" type="string" value=""/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="1e-3"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="1e-6"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="100000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="0"/>
      </Method>
    </Task>
    <Task key="Task_2" name="Scan" type="scan" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="Subtask" type="unsignedInteger" value="1"/>
        <ParameterGroup name="ScanItems">
        </ParameterGroup>
        <Parameter name="Output in subtask" type="bool" value="1"/>
        <Parameter name="Adjust initial conditions" type="bool" value="0"/>
        <Parameter name="Continue on Error" type="bool" value="0"/>
      </Problem>
      <Method name="Scan Framework" type="ScanFramework">
      </Method>
    </Task>
    <Task key="Task_3" name="Elementary Flux Modes" type="fluxMode" scheduled="false" updateModel="false">
      <Report reference="Report_1" target="" append="1" confirmOverwrite="1"/>
      <Problem>
      </Problem>
      <Method name="EFM Algorithm" type="EFMAlgorithm">
      </Method>
    </Task>
    <Task key="Task_4" name="Optimization" type="optimization" scheduled="false" updateModel="false">
      <Report reference="Report_2" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Subtask" type="cn" value="CN=Root,Vector=TaskList[Steady-State]"/>
        <ParameterText name="ObjectiveExpression" type="expression">
          
        </ParameterText>
        <Parameter name="Maximize" type="bool" value="0"/>
        <Parameter name="Randomize Start Values" type="bool" value="0"/>
        <Parameter name="Calculate Statistics" type="bool" value="1"/>
        <ParameterGroup name="OptimizationItemList">
        </ParameterGroup>
        <ParameterGroup name="OptimizationConstraintList">
        </ParameterGroup>
      </Problem>
      <Method name="Random Search" type="RandomSearch">
        <Parameter name="Log Verbosity" type="unsignedInteger" value="0"/>
        <Parameter name="Number of Iterations" type="unsignedInteger" value="100000"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_5" name="Parameter Estimation" type="parameterFitting" scheduled="false" updateModel="false">
      <Report reference="Report_3" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Maximize" type="bool" value="0"/>
        <Parameter name="Randomize Start Values" type="bool" value="0"/>
        <Parameter name="Calculate Statistics" type="bool" value="1"/>
        <ParameterGroup name="OptimizationItemList">
        </ParameterGroup>
        <ParameterGroup name="OptimizationConstraintList">
        </ParameterGroup>
        <Parameter name="Steady-State" type="cn" value="CN=Root,Vector=TaskList[Steady-State]"/>
        <Parameter name="Time-Course" type="cn" value="CN=Root,Vector=TaskList[Time-Course]"/>
        <Parameter name="Create Parameter Sets" type="bool" value="0"/>
        <ParameterGroup name="Experiment Set">
        </ParameterGroup>
        <ParameterGroup name="Validation Set">
          <Parameter name="Weight" type="unsignedFloat" value="1"/>
          <Parameter name="Threshold" type="unsignedInteger" value="5"/>
        </ParameterGroup>
      </Problem>
      <Method name="Evolutionary Programming" type="EvolutionaryProgram">
        <Parameter name="Log Verbosity" type="unsignedInteger" value="0"/>
        <Parameter name="Number of Generations" type="unsignedInteger" value="200"/>
        <Parameter name="Population Size" type="unsignedInteger" value="20"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
        <Parameter name="Stop after # Stalled Generations" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_6" name="Metabolic Control Analysis" type="metabolicControlAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_4" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_13"/>
      </Problem>
      <Method name="MCA Method (Reder)" type="MCAMethod(Reder)">
        <Parameter name="Modulation Factor" type="unsignedFloat" value="1.0000000000000001e-09"/>
        <Parameter name="Use Reder" type="bool" value="1"/>
        <Parameter name="Use Smallbone" type="bool" value="1"/>
      </Method>
    </Task>
    <Task key="Task_7" name="Lyapunov Exponents" type="lyapunovExponents" scheduled="false" updateModel="false">
      <Report reference="Report_5" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="ExponentNumber" type="unsignedInteger" value="3"/>
        <Parameter name="DivergenceRequested" type="bool" value="1"/>
        <Parameter name="TransientTime" type="float" value="0"/>
      </Problem>
      <Method name="Wolf Method" type="WolfMethod">
        <Parameter name="Orthonormalization Interval" type="unsignedFloat" value="1"/>
        <Parameter name="Overall time" type="unsignedFloat" value="1000"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="9.9999999999999998e-13"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
      </Method>
    </Task>
    <Task key="Task_8" name="Time Scale Separation Analysis" type="timeScaleSeparationAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_6" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
      </Problem>
      <Method name="ILDM (LSODA,Deuflhard)" type="TimeScaleSeparation(ILDM,Deuflhard)">
        <Parameter name="Deuflhard Tolerance" type="unsignedFloat" value="0.0001"/>
      </Method>
    </Task>
    <Task key="Task_9" name="Sensitivities" type="sensitivities" scheduled="false" updateModel="false">
      <Report reference="Report_7" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="SubtaskType" type="unsignedInteger" value="1"/>
        <ParameterGroup name="TargetFunctions">
          <Parameter name="SingleObject" type="cn" value=""/>
          <Parameter name="ObjectListType" type="unsignedInteger" value="7"/>
        </ParameterGroup>
        <ParameterGroup name="ListOfVariables">
          <ParameterGroup name="Variables">
            <Parameter name="SingleObject" type="cn" value=""/>
            <Parameter name="ObjectListType" type="unsignedInteger" value="41"/>
          </ParameterGroup>
          <ParameterGroup name="Variables">
            <Parameter name="SingleObject" type="cn" value=""/>
            <Parameter name="ObjectListType" type="unsignedInteger" value="0"/>
          </ParameterGroup>
        </ParameterGroup>
      </Problem>
      <Method name="Sensitivities Method" type="SensitivitiesMethod">
        <Parameter name="Delta factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Delta minimum" type="unsignedFloat" value="9.9999999999999998e-13"/>
      </Method>
    </Task>
    <Task key="Task_10" name="Moieties" type="moieties" scheduled="false" updateModel="false">
      <Report reference="Report_8" target="" append="1" confirmOverwrite="1"/>
      <Problem>
      </Problem>
      <Method name="Householder Reduction" type="Householder">
      </Method>
    </Task>
    <Task key="Task_11" name="Cross Section" type="crosssection" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="0"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="Use Values" type="bool" value="0"/>
        <Parameter name="Values" type="string" value=""/>
        <Parameter name="LimitCrossings" type="bool" value="0"/>
        <Parameter name="NumCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitOutTime" type="bool" value="0"/>
        <Parameter name="LimitOutCrossings" type="bool" value="0"/>
        <Parameter name="PositiveDirection" type="bool" value="1"/>
        <Parameter name="NumOutCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitUntilConvergence" type="bool" value="0"/>
        <Parameter name="ConvergenceTolerance" type="float" value="9.9999999999999995e-07"/>
        <Parameter name="Threshold" type="float" value="0"/>
        <Parameter name="DelayOutputUntilConvergence" type="bool" value="0"/>
        <Parameter name="OutputConvergenceTolerance" type="float" value="9.9999999999999995e-07"/>
        <ParameterText name="TriggerExpression" type="expression">
          
        </ParameterText>
        <Parameter name="SingleVariable" type="cn" value=""/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="9.9999999999999998e-13"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="100000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="0"/>
      </Method>
    </Task>
    <Task key="Task_12" name="Linear Noise Approximation" type="linearNoiseApproximation" scheduled="false" updateModel="false">
      <Report reference="Report_9" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_13"/>
      </Problem>
      <Method name="Linear Noise Approximation" type="LinearNoiseApproximation">
      </Method>
    </Task>
    <Task key="Task_14" name="Time-Course Sensitivities" type="timeSensitivities" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="0"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="Use Values" type="bool" value="0"/>
        <Parameter name="Values" type="string" value=""/>
        <ParameterGroup name="ListOfParameters">
        </ParameterGroup>
        <ParameterGroup name="ListOfTargets">
        </ParameterGroup>
      </Problem>
      <Method name="LSODA Sensitivities" type="Sensitivities(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="9.9999999999999995e-07"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="9.9999999999999998e-13"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="0"/>
      </Method>
    </Task>
  </ListOfTasks>
  <ListOfReports>
    <Report key="Report_0" name="Steady-State" taskType="steadyState" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Steady-State]"/>
      </Footer>
    </Report>
    <Report key="Report_1" name="Elementary Flux Modes" taskType="fluxMode" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Elementary Flux Modes],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_2" name="Optimization" taskType="optimization" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Object=Description"/>
        <Object cn="String=\[Function Evaluations\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Value\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Parameters\]"/>
      </Header>
      <Body>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Function Evaluations"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Best Value"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Problem=Optimization,Reference=Best Parameters"/>
      </Body>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Optimization],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_3" name="Parameter Estimation" taskType="parameterFitting" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Object=Description"/>
        <Object cn="String=\[Function Evaluations\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Value\]"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="String=\[Best Parameters\]"/>
      </Header>
      <Body>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Function Evaluations"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Best Value"/>
        <Object cn="Separator=&#x09;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Problem=Parameter Estimation,Reference=Best Parameters"/>
      </Body>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Parameter Estimation],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_4" name="Metabolic Control Analysis" taskType="metabolicControlAnalysis" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Metabolic Control Analysis],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Metabolic Control Analysis],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_5" name="Lyapunov Exponents" taskType="lyapunovExponents" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Lyapunov Exponents],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Lyapunov Exponents],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_6" name="Time Scale Separation Analysis" taskType="timeScaleSeparationAnalysis" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Time Scale Separation Analysis],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Time Scale Separation Analysis],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_7" name="Sensitivities" taskType="sensitivities" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Sensitivities],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Sensitivities],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_8" name="Moieties" taskType="moieties" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Moieties],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Moieties],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_9" name="Linear Noise Approximation" taskType="linearNoiseApproximation" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Header>
        <Object cn="CN=Root,Vector=TaskList[Linear Noise Approximation],Object=Description"/>
      </Header>
      <Footer>
        <Object cn="String=&#x0a;"/>
        <Object cn="CN=Root,Vector=TaskList[Linear Noise Approximation],Object=Result"/>
      </Footer>
    </Report>
 <Report key="Report_10" name="Time course" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
      </Table>
    </Report>
  </ListOfReports>
  <GUI>
  </GUI>
  <SBMLReference file="Bertram2004.xml">
    <SBMLMap SBMLid="COMpartment" COPASIkey="Compartment_3"/>
    <SBMLMap SBMLid="Cm" COPASIkey="ModelValue_266"/>
    <SBMLMap SBMLid="ICa" COPASIkey="ModelValue_264"/>
    <SBMLMap SBMLid="IK" COPASIkey="ModelValue_263"/>
    <SBMLMap SBMLid="IKATP" COPASIkey="ModelValue_108"/>
    <SBMLMap SBMLid="IKCa" COPASIkey="ModelValue_265"/>
    <SBMLMap SBMLid="Jer" COPASIkey="ModelValue_277"/>
    <SBMLMap SBMLid="Jleak" COPASIkey="ModelValue_180"/>
    <SBMLMap SBMLid="Jmem" COPASIkey="ModelValue_276"/>
    <SBMLMap SBMLid="Jserca" COPASIkey="ModelValue_181"/>
    <SBMLMap SBMLid="Kserca" COPASIkey="ModelValue_281"/>
    <SBMLMap SBMLid="Rgk" COPASIkey="ModelValue_178"/>
    <SBMLMap SBMLid="V" COPASIkey="Metabolite_16"/>
    <SBMLMap SBMLid="VCa" COPASIkey="ModelValue_272"/>
    <SBMLMap SBMLid="VK" COPASIkey="ModelValue_107"/>
    <SBMLMap SBMLid="adp" COPASIkey="Metabolite_10"/>
    <SBMLMap SBMLid="adp3m" COPASIkey="ModelValue_113"/>
    <SBMLMap SBMLid="alpha" COPASIkey="ModelValue_284"/>
    <SBMLMap SBMLid="amp" COPASIkey="ModelValue_94"/>
    <SBMLMap SBMLid="atot" COPASIkey="ModelValue_177"/>
    <SBMLMap SBMLid="atp" COPASIkey="ModelValue_102"/>
    <SBMLMap SBMLid="atp4m" COPASIkey="ModelValue_112"/>
    <SBMLMap SBMLid="bottom1" COPASIkey="ModelValue_172"/>
    <SBMLMap SBMLid="bottom10" COPASIkey="ModelValue_134"/>
    <SBMLMap SBMLid="bottom11" COPASIkey="ModelValue_131"/>
    <SBMLMap SBMLid="bottom12" COPASIkey="ModelValue_128"/>
    <SBMLMap SBMLid="bottom13" COPASIkey="ModelValue_125"/>
    <SBMLMap SBMLid="bottom14" COPASIkey="ModelValue_122"/>
    <SBMLMap SBMLid="bottom15" COPASIkey="ModelValue_119"/>
    <SBMLMap SBMLid="bottom16" COPASIkey="ModelValue_116"/>
    <SBMLMap SBMLid="bottom2" COPASIkey="ModelValue_163"/>
    <SBMLMap SBMLid="bottom3" COPASIkey="ModelValue_160"/>
    <SBMLMap SBMLid="bottom4" COPASIkey="ModelValue_152"/>
    <SBMLMap SBMLid="bottom5" COPASIkey="ModelValue_149"/>
    <SBMLMap SBMLid="bottom6" COPASIkey="ModelValue_146"/>
    <SBMLMap SBMLid="bottom7" COPASIkey="ModelValue_143"/>
    <SBMLMap SBMLid="bottom8" COPASIkey="ModelValue_140"/>
    <SBMLMap SBMLid="bottom9" COPASIkey="ModelValue_137"/>
    <SBMLMap SBMLid="bottomo" COPASIkey="ModelValue_110"/>
    <SBMLMap SBMLid="c" COPASIkey="Metabolite_14"/>
    <SBMLMap SBMLid="cat" COPASIkey="ModelValue_166"/>
    <SBMLMap SBMLid="cer" COPASIkey="Metabolite_13"/>
    <SBMLMap SBMLid="epser" COPASIkey="ModelValue_283"/>
    <SBMLMap SBMLid="f6p" COPASIkey="ModelValue_175"/>
    <SBMLMap SBMLid="famp" COPASIkey="ModelValue_159"/>
    <SBMLMap SBMLid="fatp" COPASIkey="ModelValue_158"/>
    <SBMLMap SBMLid="fback" COPASIkey="ModelValue_101"/>
    <SBMLMap SBMLid="fbp" COPASIkey="Metabolite_11"/>
    <SBMLMap SBMLid="fbt" COPASIkey="ModelValue_156"/>
    <SBMLMap SBMLid="fcyt" COPASIkey="ModelValue_275"/>
    <SBMLMap SBMLid="fer" COPASIkey="ModelValue_278"/>
    <SBMLMap SBMLid="ffbp" COPASIkey="ModelValue_157"/>
    <SBMLMap SBMLid="fmt" COPASIkey="ModelValue_155"/>
    <SBMLMap SBMLid="g6p" COPASIkey="Metabolite_12"/>
    <SBMLMap SBMLid="gCa" COPASIkey="ModelValue_270"/>
    <SBMLMap SBMLid="gK" COPASIkey="ModelValue_267"/>
    <SBMLMap SBMLid="gKCa" COPASIkey="ModelValue_268"/>
    <SBMLMap SBMLid="gkatpbar" COPASIkey="ModelValue_106"/>
    <SBMLMap SBMLid="k1" COPASIkey="ModelValue_170"/>
    <SBMLMap SBMLid="k2" COPASIkey="ModelValue_169"/>
    <SBMLMap SBMLid="k3" COPASIkey="ModelValue_168"/>
    <SBMLMap SBMLid="k4" COPASIkey="ModelValue_167"/>
    <SBMLMap SBMLid="katpo" COPASIkey="ModelValue_109"/>
    <SBMLMap SBMLid="kd" COPASIkey="ModelValue_269"/>
    <SBMLMap SBMLid="kdd" COPASIkey="ModelValue_105"/>
    <SBMLMap SBMLid="kg" COPASIkey="ModelValue_95"/>
    <SBMLMap SBMLid="kpmca" COPASIkey="ModelValue_182"/>
    <SBMLMap SBMLid="ktd" COPASIkey="ModelValue_104"/>
    <SBMLMap SBMLid="ktt" COPASIkey="ModelValue_103"/>
    <SBMLMap SBMLid="lambda" COPASIkey="ModelValue_174"/>
    <SBMLMap SBMLid="lambdaer" COPASIkey="ModelValue_282"/>
    <SBMLMap SBMLid="mgadp" COPASIkey="ModelValue_114"/>
    <SBMLMap SBMLid="minf" COPASIkey="ModelValue_271"/>
    <SBMLMap SBMLid="n" COPASIkey="Metabolite_15"/>
    <SBMLMap SBMLid="ninf" COPASIkey="ModelValue_274"/>
    <SBMLMap SBMLid="pfk" COPASIkey="ModelValue_173"/>
    <SBMLMap SBMLid="pfkbas" COPASIkey="ModelValue_176"/>
    <SBMLMap SBMLid="pleak" COPASIkey="ModelValue_280"/>
    <SBMLMap SBMLid="r" COPASIkey="ModelValue_98"/>
    <SBMLMap SBMLid="r1" COPASIkey="ModelValue_99"/>
    <SBMLMap SBMLid="rad" COPASIkey="ModelValue_93"/>
    <SBMLMap SBMLid="ratio" COPASIkey="ModelValue_92"/>
    <SBMLMap SBMLid="rgpdh" COPASIkey="ModelValue_179"/>
    <SBMLMap SBMLid="sigmaV" COPASIkey="ModelValue_279"/>
    <SBMLMap SBMLid="taua" COPASIkey="ModelValue_100"/>
    <SBMLMap SBMLid="taun" COPASIkey="ModelValue_273"/>
    <SBMLMap SBMLid="topa1" COPASIkey="ModelValue_171"/>
    <SBMLMap SBMLid="topa10" COPASIkey="ModelValue_135"/>
    <SBMLMap SBMLid="topa11" COPASIkey="ModelValue_132"/>
    <SBMLMap SBMLid="topa12" COPASIkey="ModelValue_129"/>
    <SBMLMap SBMLid="topa13" COPASIkey="ModelValue_126"/>
    <SBMLMap SBMLid="topa14" COPASIkey="ModelValue_123"/>
    <SBMLMap SBMLid="topa15" COPASIkey="ModelValue_120"/>
    <SBMLMap SBMLid="topa16" COPASIkey="ModelValue_117"/>
    <SBMLMap SBMLid="topa2" COPASIkey="ModelValue_164"/>
    <SBMLMap SBMLid="topa3" COPASIkey="ModelValue_162"/>
    <SBMLMap SBMLid="topa4" COPASIkey="ModelValue_153"/>
    <SBMLMap SBMLid="topa5" COPASIkey="ModelValue_150"/>
    <SBMLMap SBMLid="topa6" COPASIkey="ModelValue_147"/>
    <SBMLMap SBMLid="topa7" COPASIkey="ModelValue_144"/>
    <SBMLMap SBMLid="topa8" COPASIkey="ModelValue_141"/>
    <SBMLMap SBMLid="topa9" COPASIkey="ModelValue_138"/>
    <SBMLMap SBMLid="topb" COPASIkey="ModelValue_115"/>
    <SBMLMap SBMLid="topo" COPASIkey="ModelValue_111"/>
    <SBMLMap SBMLid="vg" COPASIkey="ModelValue_96"/>
    <SBMLMap SBMLid="weight10" COPASIkey="ModelValue_136"/>
    <SBMLMap SBMLid="weight11" COPASIkey="ModelValue_133"/>
    <SBMLMap SBMLid="weight12" COPASIkey="ModelValue_130"/>
    <SBMLMap SBMLid="weight13" COPASIkey="ModelValue_127"/>
    <SBMLMap SBMLid="weight14" COPASIkey="ModelValue_124"/>
    <SBMLMap SBMLid="weight15" COPASIkey="ModelValue_121"/>
    <SBMLMap SBMLid="weight16" COPASIkey="ModelValue_118"/>
    <SBMLMap SBMLid="weight2" COPASIkey="ModelValue_165"/>
    <SBMLMap SBMLid="weight3" COPASIkey="ModelValue_161"/>
    <SBMLMap SBMLid="weight4" COPASIkey="ModelValue_154"/>
    <SBMLMap SBMLid="weight5" COPASIkey="ModelValue_151"/>
    <SBMLMap SBMLid="weight6" COPASIkey="ModelValue_148"/>
    <SBMLMap SBMLid="weight7" COPASIkey="ModelValue_145"/>
    <SBMLMap SBMLid="weight8" COPASIkey="ModelValue_142"/>
    <SBMLMap SBMLid="weight9" COPASIkey="ModelValue_139"/>
    <SBMLMap SBMLid="y" COPASIkey="ModelValue_97"/>
  </SBMLReference>
  <ListOfUnitDefinitions>
    <UnitDefinition key="Unit_1" name="meter" symbol="m">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_0">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:53:18Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        m
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_5" name="second" symbol="s">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_4">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:53:18Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        s
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_13" name="Avogadro" symbol="Avogadro">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_12">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:53:18Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Avogadro
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_17" name="item" symbol="#">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_16">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:53:18Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        #
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_35" name="liter" symbol="l">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_34">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:53:18Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        0.001*m^3
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_41" name="mole" symbol="mol">
      <MiriamAnnotation>
<rdf:RDF
xmlns:dcterms="http://purl.org/dc/terms/"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<rdf:Description rdf:about="#Unit_40">
<dcterms:created>
<rdf:Description>
<dcterms:W3CDTF>2020-12-11T09:53:18Z</dcterms:W3CDTF>
</rdf:Description>
</dcterms:created>
</rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Avogadro*#
      </Expression>
    </UnitDefinition>
  </ListOfUnitDefinitions>
</COPASI>
