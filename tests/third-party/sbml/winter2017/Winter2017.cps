<?xml version="1.0" encoding="UTF-8"?>
<!-- generated with COPASI 4.20 (Build 145) (http://www.copasi.org) at 2017-03-01 17:27:24 UTC -->
<?oxygen RNGSchema="http://www.copasi.org/static/schema/CopasiML.rng" type="xml"?>
<COPASI xmlns="http://www.copasi.org/static/schema" versionMajor="4" versionMinor="20" versionDevel="145" copasiSourcesModified="0">
  <ListOfFunctions>
    <Function key="Function_13" name="Mass action (irreversible)" type="MassAction" reversible="false">
      <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
   <rdf:Description rdf:about="#Function_13">
   <CopasiMT:is rdf:resource="urn:miriam:obo.sbo:SBO:0000041" />
   </rdf:Description>
   </rdf:RDF>
      </MiriamAnnotation>
      <Comment>
        <body xmlns="http://www.w3.org/1999/xhtml">
<b>Mass action rate law for first order irreversible reactions</b>
<p>
Reaction scheme where the products are created from the reactants and the change of a product quantity is proportional to the product of reactant activities. The reaction scheme does not include any reverse process that creates the reactants from the products. The change of a product quantity is proportional to the quantity of one reactant.
</p>
</body>
      </Comment>
      <Expression>
        k1*PRODUCT&lt;substrate_i>
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_81" name="k1" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_79" name="substrate" order="1" role="substrate"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_14" name="Mass action (reversible)" type="MassAction" reversible="true">
      <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
   <rdf:Description rdf:about="#Function_14">
   <CopasiMT:is rdf:resource="urn:miriam:obo.sbo:SBO:0000042" />
   </rdf:Description>
   </rdf:RDF>
      </MiriamAnnotation>
      <Comment>
        <body xmlns="http://www.w3.org/1999/xhtml">
<b>Mass action rate law for reversible reactions</b>
<p>
Reaction scheme where the products are created from the reactants and the change of a product quantity is proportional to the product of reactant activities. The reaction scheme does include a reverse process that creates the reactants from the products.
</p>
</body>
      </Comment>
      <Expression>
        k1*PRODUCT&lt;substrate_i>-k2*PRODUCT&lt;product_j>
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_62" name="k1" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_39" name="substrate" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_67" name="k2" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_71" name="product" order="3" role="product"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_40" name="vdHb_in [1]" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_40">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-24T11:34:53Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        F_in * (O2_a - (2 * O2_c - O2_a))
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_264" name="F_in" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_254" name="O2_a" order="1" role="modifier"/>
        <ParameterDescription key="FunctionParameter_258" name="O2_c" order="2" role="modifier"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_41" name="vdHb_out" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_41">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-24T11:41:20Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        F_out * dHb/V_v
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_265" name="F_out" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_262" name="dHb" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_267" name="V_v" order="2" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_42" name="vATPase" type="UserDefined" reversible="false">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_42">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:03:14Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        VmaxATPase * (ATP/(ATP+Km_ATP))
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_268" name="VmaxATPase" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_246" name="ATP" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_270" name="Km_ATP" order="2" role="constant"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_43" name="vPK" type="UserDefined" reversible="false">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_43">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:07:33Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        k_PK*PEP*ADP
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_271" name="k_PK" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_266" name="PEP" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_273" name="ADP" order="2" role="substrate"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_44" name="vPGK" type="UserDefined" reversible="false">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_44">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:10:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        k_PGK*GAP*ADP*(NAD/NADH)
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_274" name="k_PGK" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_269" name="GAP" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_276" name="ADP" order="2" role="substrate"/>
        <ParameterDescription key="FunctionParameter_278" name="NAD" order="3" role="substrate"/>
        <ParameterDescription key="FunctionParameter_280" name="NADH" order="4" role="product"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_45" name="vPFK" type="UserDefined" reversible="false">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_45">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-30T14:36:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        k_PFK*ATP*(1+(ATP/K_I_ATP)^nH)^(-1)*(F6P/(F6P+K_m_F6P))
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_281" name="k_PFK" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_277" name="ATP" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_272" name="K_I_ATP" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_283" name="nH" order="3" role="constant"/>
        <ParameterDescription key="FunctionParameter_285" name="F6P" order="4" role="substrate"/>
        <ParameterDescription key="FunctionParameter_287" name="K_m_F6P" order="5" role="constant"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_46" name="facilitated transport (inkl. Volume) " type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_46">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T11:26:59Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Vmax * (S/(S+K)-P/(P+K)) * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_288" name="Vmax" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_284" name="S" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_275" name="K" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_289" name="P" order="3" role="product"/>
        <ParameterDescription key="FunctionParameter_291" name="Volume" order="4" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_47" name="vGLU_eg (inkl. Volumes)" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_47">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T09:04:20Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Vmax_GLU * (GLU_e/(GLU_e + K_m_GLU)) * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_292" name="Vmax_GLU" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_279" name="GLU_e" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_286" name="K_m_GLU" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_294" name="Volume" order="3" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_48" name="vGLU_gn (inkl. Volume)" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_48">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T09:12:26Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Vmax_GLU * (GLU_g/(GLU_g+K_m_GLU))*(ATP_g/(ATP_g+K_m_ATP)) * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_295" name="Vmax_GLU" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_282" name="GLU_g" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_296" name="K_m_GLU" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_298" name="ATP_g" order="3" role="substrate"/>
        <ParameterDescription key="FunctionParameter_300" name="K_m_ATP" order="4" role="constant"/>
        <ParameterDescription key="FunctionParameter_302" name="Volume" order="5" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_49" name="vGLU_ne (inkl. Volume)" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_49">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T08:57:29Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        vSTIM * ratio_Na_GLU * (GLU_n/(GLU_n + Km_GLU)) * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_303" name="vSTIM" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_299" name="ratio_Na_GLU" order="1" role="constant"/>
        <ParameterDescription key="FunctionParameter_290" name="GLU_n" order="2" role="substrate"/>
        <ParameterDescription key="FunctionParameter_304" name="Km_GLU" order="3" role="constant"/>
        <ParameterDescription key="FunctionParameter_306" name="Volume" order="4" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_50" name="vStim (with volume)" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_50">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2015-01-26T11:59:07Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        vstim * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_307" name="vstim" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_293" name="Volume" order="1" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_51" name="modular rate law for two substrates, two products" type="UserDefined" reversible="unspecified">
      <Expression>
        Vmax * (1/(K_S1 * K_S2)) * ((S1 * S2 - (P1 * P2)/Keq)/((1+S1/K_S1)*(1+S2/K_S2)+(1+P1/K_P1)*(1+P2/K_P2) -1 ))
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_297" name="Vmax" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_301" name="K_S1" order="1" role="constant"/>
        <ParameterDescription key="FunctionParameter_309" name="K_S2" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_311" name="S1" order="3" role="substrate"/>
        <ParameterDescription key="FunctionParameter_313" name="S2" order="4" role="substrate"/>
        <ParameterDescription key="FunctionParameter_315" name="P1" order="5" role="product"/>
        <ParameterDescription key="FunctionParameter_317" name="P2" order="6" role="product"/>
        <ParameterDescription key="FunctionParameter_319" name="Keq" order="7" role="constant"/>
        <ParameterDescription key="FunctionParameter_321" name="K_P1" order="8" role="constant"/>
        <ParameterDescription key="FunctionParameter_323" name="K_P2" order="9" role="constant"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_52" name="modular rate law for one substrate, one product" type="UserDefined" reversible="unspecified">
      <Expression>
        Vmax * (1/(K_S1)) *( (S1 - P1/Keq)/((1+S1/K_S1)+(1+P1/K_P1)-1))
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_324" name="Vmax" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_320" name="K_S1" order="1" role="constant"/>
        <ParameterDescription key="FunctionParameter_316" name="S1" order="2" role="substrate"/>
        <ParameterDescription key="FunctionParameter_312" name="P1" order="3" role="product"/>
        <ParameterDescription key="FunctionParameter_308" name="Keq" order="4" role="constant"/>
        <ParameterDescription key="FunctionParameter_325" name="K_P1" order="5" role="constant"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_53" name="Blood flow contribution inkl. volume" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_53">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T11:24:07Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        ((2*F_in)/V_c)*(Substrate - Product) * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_326" name="F_in" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_310" name="V_c" order="1" role="volume"/>
        <ParameterDescription key="FunctionParameter_329" name="Volume" order="2" role="volume"/>
        <ParameterDescription key="FunctionParameter_318" name="Substrate" order="3" role="substrate"/>
        <ParameterDescription key="FunctionParameter_327" name="Product" order="4" role="product"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_54" name="O2 transport function inkl. volume" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_54">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:43:34Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        (PScap/Volume) *((KO2*(HbOP/O2_source -1)^(-1/nh))-O2_destination) * Volume1
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_330" name="PScap" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_322" name="Volume" order="1" role="volume"/>
        <ParameterDescription key="FunctionParameter_305" name="KO2" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_332" name="HbOP" order="3" role="constant"/>
        <ParameterDescription key="FunctionParameter_334" name="O2_source" order="4" role="substrate"/>
        <ParameterDescription key="FunctionParameter_336" name="nh" order="5" role="constant"/>
        <ParameterDescription key="FunctionParameter_338" name="O2_destination" order="6" role="product"/>
        <ParameterDescription key="FunctionParameter_340" name="Volume1" order="7" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_55" name="vLEAK_Na inkl. Volume" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_55">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:13:15Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        (Sm* gNA )/(Volume * F)*((RT/F)*log(Na_e/Na) - Vm) * Volume1
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_341" name="Sm" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_337" name="gNA" order="1" role="constant"/>
        <ParameterDescription key="FunctionParameter_333" name="Volume" order="2" role="volume"/>
        <ParameterDescription key="FunctionParameter_314" name="F" order="3" role="constant"/>
        <ParameterDescription key="FunctionParameter_342" name="RT" order="4" role="constant"/>
        <ParameterDescription key="FunctionParameter_344" name="Na_e" order="5" role="substrate"/>
        <ParameterDescription key="FunctionParameter_346" name="Na" order="6" role="product"/>
        <ParameterDescription key="FunctionParameter_348" name="Vm" order="7" role="constant"/>
        <ParameterDescription key="FunctionParameter_350" name="Volume1" order="8" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_56" name="vPUMP volume dependent" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_56">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T16:37:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Sm/Volume * k_pump * ATP * Na * (1+ATP/Km_pump)^-1
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_351" name="Sm" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_347" name="Volume" order="1" role="volume"/>
        <ParameterDescription key="FunctionParameter_343" name="k_pump" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_331" name="ATP" order="3" role="substrate"/>
        <ParameterDescription key="FunctionParameter_339" name="Na" order="4" role="substrate"/>
        <ParameterDescription key="FunctionParameter_353" name="Km_pump" order="5" role="constant"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_57" name="vHK (HS)" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_57">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-30T14:02:38Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        k_HK*ATP * (1+G6P/K_I_G6P)^-1
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_354" name="k_HK" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_335" name="ATP" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_345" name="G6P" order="2" role="modifier"/>
        <ParameterDescription key="FunctionParameter_355" name="K_I_G6P" order="3" role="constant"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_58" name="facilitated transport (inkl. Volume)  (scaled)" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_58">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T11:26:59Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        Vmax * sf * (S/(S+K)-P/(P+K)) * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_356" name="Vmax" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_328" name="sf" order="1" role="constant"/>
        <ParameterDescription key="FunctionParameter_357" name="S" order="2" role="substrate"/>
        <ParameterDescription key="FunctionParameter_359" name="K" order="3" role="constant"/>
        <ParameterDescription key="FunctionParameter_361" name="P" order="4" role="product"/>
        <ParameterDescription key="FunctionParameter_363" name="Volume" order="5" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
    <Function key="Function_59" name="vMITO2 (inkl. Volumes)" type="UserDefined" reversible="unspecified">
      <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Function_59">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-30T14:32:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
      </MiriamAnnotation>
      <Expression>
        v_max_mito*(PYR/(PYR+K_m_PYR))*(ADP/(ADP+K_m_ADP))*(O2/(O2+K_m_O2))*(1-1/(1+exp(-alpha*(ATP/ADP-beta)))) * Volume
      </Expression>
      <ListOfParameterDescriptions>
        <ParameterDescription key="FunctionParameter_364" name="v_max_mito" order="0" role="constant"/>
        <ParameterDescription key="FunctionParameter_360" name="PYR" order="1" role="substrate"/>
        <ParameterDescription key="FunctionParameter_352" name="K_m_PYR" order="2" role="constant"/>
        <ParameterDescription key="FunctionParameter_365" name="ADP" order="3" role="substrate"/>
        <ParameterDescription key="FunctionParameter_367" name="K_m_ADP" order="4" role="constant"/>
        <ParameterDescription key="FunctionParameter_369" name="O2" order="5" role="substrate"/>
        <ParameterDescription key="FunctionParameter_371" name="K_m_O2" order="6" role="constant"/>
        <ParameterDescription key="FunctionParameter_373" name="alpha" order="7" role="constant"/>
        <ParameterDescription key="FunctionParameter_375" name="ATP" order="8" role="product"/>
        <ParameterDescription key="FunctionParameter_377" name="beta" order="9" role="constant"/>
        <ParameterDescription key="FunctionParameter_379" name="Volume" order="10" role="volume"/>
      </ListOfParameterDescriptions>
    </Function>
  </ListOfFunctions>
  <Model key="Model_4" name="Brain Energy Metabolism with PPP" simulationType="time" timeUnit="s" volumeUnit="ml" areaUnit="m²" lengthUnit="m" quantityUnit="mmol" type="deterministic" avogadroConstant="6.02214179e+023">
    <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
   xmlns:vCard="http://www.w3.org/2001/vcard-rdf/3.0#">
  <rdf:Description rdf:about="#Model_4">
    <dcterms:bibliographicCitation>
      <rdf:Description>
        <CopasiMT:isDescribedBy rdf:resource="urn:miriam:doi:10.1177/0271678X17693024"/>
      </rdf:Description>
    </dcterms:bibliographicCitation>
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-07-30T11:50:11Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <dcterms:creator>
      <rdf:Description>
        <vCard:EMAIL>felix.winter@asd-online.com</vCard:EMAIL>
        <vCard:N>
          <rdf:Description>
            <vCard:Family>Winter</vCard:Family>
            <vCard:Given>Felix</vCard:Given>
          </rdf:Description>
        </vCard:N>
        <vCard:ORG>
          <rdf:Description>
            <vCard:Orgname>ASD GmbH</vCard:Orgname>
          </rdf:Description>
        </vCard:ORG>
      </rdf:Description>
    </dcterms:creator>
    <dcterms:modified>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T09:29:38</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:modified>
    <CopasiMT:is rdf:resource="urn:miriam:biomodels.db:MODEL1603240000"/>
  </rdf:Description>
</rdf:RDF>

    </MiriamAnnotation>
    <Comment>
      This model is part of the following publication:

Mathematical Analysis of the Influence of Brain Metabolism on the BOLD signal in Alzheimer's Disease
Authors: Felix Winter, Catrin Bludszuweit-Philipp, Olaf Wolkenhauer

Journal of Cerebral Blood Flow and Metabolism (2017)
    </Comment>
    <ListOfCompartments>
      <Compartment key="Compartment_1" name="capillary" simulationType="fixed" dimensionality="3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:05:36Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000290" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Compartment>
      <Compartment key="Compartment_3" name="neurons" simulationType="fixed" dimensionality="3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:05:51Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0005623" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000290" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Compartment>
      <Compartment key="Compartment_5" name="astrocytes" simulationType="fixed" dimensionality="3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_5">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:33:57Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0005623" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000290" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Compartment>
      <Compartment key="Compartment_7" name="extracellular_space" simulationType="fixed" dimensionality="3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_7">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:05:44Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0005615" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000290" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Compartment>
      <Compartment key="Compartment_9" name="venous balloon" simulationType="ode" dimensionality="3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_9">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T10:51:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0005623" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000290" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=Value>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_out],Reference=Value>
        </Expression>
      </Compartment>
      <Compartment key="Compartment_11" name="artery" simulationType="fixed" dimensionality="3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Compartment_11">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T11:39:08Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000290" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Compartment>
    </ListOfCompartments>
    <ListOfMetabolites>
      <Metabolite key="Metabolite_1" name="O2" simulationType="reactions" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-09-18T08:56:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15379" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_3" name="CO2" simulationType="reactions" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-09-18T08:56:26Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16526" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_5" name="GLC" simulationType="reactions" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_5">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-09-18T08:56:45Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:17634" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_7" name="LAC" simulationType="reactions" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_7">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:17:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:422" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_9" name="dHb" simulationType="reactions" compartment="Compartment_1">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_9">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-24T12:01:20Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:5656" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_11" name="GLC" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_11">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:17:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:17634" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_13" name="G6P" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_13">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:16:22Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:48928" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_15" name="ATP" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_15">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:14:36Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15422" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_17" name="F6P" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_17">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:16:09Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:78697" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_19" name="GAP" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_19">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:17:01Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:48928" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_21" name="NADH" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_21">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:00:30Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16908" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_23" name="PEP" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_23">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:14:49Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:18021" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_25" name="PYR" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_25">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:06:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:32816" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_27" name="LAC" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_27">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:16:37Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:422" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_29" name="O2" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_29">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T12:39:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15379" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_31" name="PCr" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_31">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:26:29Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:17287" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          <body xmlns="http://www.w3.org/1999/xhtml">
    <pre>Phosphocreatine</pre>
  </body>
        </Comment>
      </Metabolite>
      <Metabolite key="Metabolite_33" name="Na+" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_33">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:06:14Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:29101" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_35" name="GLU" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_35">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T11:15:28Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16015" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_37" name="ADP" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_37">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:12:36Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16761" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_39" name="AMP" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_39">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:53:39Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16027" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <InitialExpression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[ANP],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ADP],Reference=InitialConcentration>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ATP],Reference=InitialConcentration>
        </InitialExpression>
      </Metabolite>
      <Metabolite key="Metabolite_41" name="Cr" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_41">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-17T09:53:04Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16919" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <InitialExpression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PCr_total],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PCr],Reference=InitialConcentration>
        </InitialExpression>
      </Metabolite>
      <Metabolite key="Metabolite_43" name="NAD" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_43">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-17T11:00:20Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:13389" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <InitialExpression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_neurons],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADH],Reference=InitialConcentration>
        </InitialExpression>
      </Metabolite>
      <Metabolite key="Metabolite_45" name="G6L" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_45">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:08:07Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16938" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_47" name="P6G" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_47">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:08:50Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16938" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_49" name="Ru5P" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_49">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:10:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:37455" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_51" name="X5P" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_51">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:10:57Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:27354" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_53" name="R5P" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_53">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:11:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:37455" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_55" name="S7P" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_55">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:12:35Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15721" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_57" name="E4P" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_57">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:13:34Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:48153" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_59" name="NADPH" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_59">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2015-07-27T14:16:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16474" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_61" name="NADP" simulationType="reactions" compartment="Compartment_3">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_61">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2015-07-27T14:16:11Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:25523" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_63" name="GLC" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_63">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-07-31T13:47:15Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:17634" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_65" name="ATP" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_65">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-07-31T13:46:39Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15422" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_67" name="G6P" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_67">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-07-31T13:47:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:48928" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_69" name="F6P" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_69">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-07-31T13:55:48Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:78697" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_71" name="GAP" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_71">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:02:35Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:48928" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_73" name="NADH" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_73">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:00:31Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16908" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_75" name="PEP" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_75">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:14:32Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:18021" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_77" name="PYR" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_77">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:06:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:32816" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_79" name="LAC" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_79">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:16:37Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:422" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_81" name="O2" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_81">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T12:39:22Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15379" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_83" name="PCr" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_83">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:26:40Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:17287" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          <body xmlns="http://www.w3.org/1999/xhtml">
    <pre>Phosphocreatine</pre>
  </body>
        </Comment>
      </Metabolite>
      <Metabolite key="Metabolite_85" name="Na+" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_85">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T16:44:02Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:29101" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_87" name="GLU" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_87">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T11:13:56Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16015" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_89" name="ADP" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_89">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:14:50Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16761" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_91" name="AMP" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_91">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:53:36Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16027" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <InitialExpression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[ANP],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ADP],Reference=InitialConcentration>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP],Reference=InitialConcentration>
        </InitialExpression>
      </Metabolite>
      <Metabolite key="Metabolite_93" name="Cr" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_93">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-17T09:50:07Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16919" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <InitialExpression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PCr_total],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PCr],Reference=InitialConcentration>
        </InitialExpression>
      </Metabolite>
      <Metabolite key="Metabolite_95" name="NAD" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_95">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-17T11:00:20Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:13389" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <InitialExpression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_astrocytes],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADH],Reference=InitialConcentration>
        </InitialExpression>
      </Metabolite>
      <Metabolite key="Metabolite_97" name="G6L" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_97">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:08:07Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16938" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_99" name="P6G" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_99">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:09:01Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16938" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_101" name="Ru5P" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_101">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:10:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:37455" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_103" name="X5P" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_103">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:10:57Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:27354" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_105" name="R5P" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_105">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:11:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:37455" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_107" name="S7P" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_107">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:12:35Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15721" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_109" name="E4P" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_109">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-21T13:13:34Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:48153" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_111" name="NADP" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_111">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2015-07-27T14:16:05Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:25523" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_113" name="NADPH" simulationType="reactions" compartment="Compartment_5">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_113">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2015-07-27T14:15:55Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16474" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_115" name="GLC" simulationType="reactions" compartment="Compartment_7">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_115">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-09-18T08:53:52Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:17634" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_117" name="LAC" simulationType="reactions" compartment="Compartment_7">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_117">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-09-18T08:54:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:422" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_119" name="GLU" simulationType="reactions" compartment="Compartment_7">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_119">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T09:58:37Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16015" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_121" name="Na+" simulationType="fixed" compartment="Compartment_7">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_121">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T09:56:21Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:29101" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_123" name="O2" simulationType="fixed" compartment="Compartment_11">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_123">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-28T14:20:06Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:15379" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_125" name="CO2" simulationType="fixed" compartment="Compartment_11">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_125">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T11:47:28Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:16526" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_127" name="GLC" simulationType="fixed" compartment="Compartment_11">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_127">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T11:04:27Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:17634" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
      <Metabolite key="Metabolite_129" name="LAC" simulationType="fixed" compartment="Compartment_11">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Metabolite_129">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T09:58:34Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:chebi:CHEBI:422" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </Metabolite>
    </ListOfMetabolites>
    <ListOfModelValues>
      <ModelValue key="ModelValue_0" name="K_m_GLC" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_0">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-09T12:39:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          <body xmlns="http://www.w3.org/1999/xhtml">
    <pre>Affinity constant for GLC</pre>
  </body>
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_1" name="K_m_G6P" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-09T12:47:05Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          <body xmlns="http://www.w3.org/1999/xhtml">
    <pre>Affinity constant for G6P</pre>
  </body>
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_2" name="K_m_F6P_PGI" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_2">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-09T12:49:28Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_3" name="K_m_F6P_PFK" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T08:45:35Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_4" name="K_I_ATP" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_4">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:23:49Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          <body xmlns="http://www.w3.org/1999/xhtml">
    <pre>Inhibition constant for ATP</pre>
  </body>
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_5" name="nH" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_5">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T12:06:01Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is>
      <rdf:Bag>
        <rdf:li rdf:resource="urn:miriam:biomodels.sbo:0000190" />
      </rdf:Bag>
    </CopasiMT:is>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          <body xmlns="http://www.w3.org/1999/xhtml">
    <pre>Hill coefficient for ATP</pre>
  </body>
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_6" name="NADH_total_neurons" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_6">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:05:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_7" name="NADH_total_astrocytes" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_7">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:05:41Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_8" name="NAD_neurons" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_8">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:03:59Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_neurons],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADH],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_9" name="NAD_astrocytes" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_9">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:04:53Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_astrocytes],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADH],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_10" name="ANP" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_10">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:32:08Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_11" name="q_AK" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_11">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:26:29Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_12" name="K_m_ATP" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_12">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:35:06Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_13" name="K_m_ADP" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_13">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T12:54:26Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_14" name="K_m_O2" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_14">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T12:54:49Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_15" name="K_m_PYR" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_15">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T12:56:11Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_16" name="PCr_total" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_16">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:24:41Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_17" name="Cr_neurons" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_17">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:27:51Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PCr_total],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PCr],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_18" name="Cr_astrocytes" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_18">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:28:26Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PCr_total],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PCr],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_19" name="Vmax_ATPase_neurons" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_19">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:21:52Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_20" name="Vmax_ATPase_astrocytes" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_20">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:21:05Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_21" name="Vmax_ec_LAC (wrt extracellular space)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_21">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:26:56Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_22" name="F_0" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_22">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-16T16:08:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          cerebral blood flow in resting condition
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_23" name="delta_F" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_23">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T10:54:57Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_24" name="t_0" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_24">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T12:10:49Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_25" name="t_end" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_25">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T11:28:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_26" name="t_1" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_26">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T12:33:58Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_27" name="F_in" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_27">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T10:45:47Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_0],Reference=Value>*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[f_CBF_dyn],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_28" name="F_out" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_28">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T10:47:08Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_0],Reference=Value>*(((&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>)^(1/0.5)+(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>)^(-1/2)*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[tau_v],Reference=Value>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>)*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=Value>)/(1+&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_0],Reference=Value>*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>)^(-1/2)*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[tau_v],Reference=Value>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>)))
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_29" name="tau_v" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_29">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:09:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_30" name="Hb.OP" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_30">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:31:13Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          O2 concentration with hemoglobin
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_31" name="Sm_g" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_31">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T16:33:19Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          characteristic length for Na (astrocytes)
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_32" name="Sm_n" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_32">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T16:34:05Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          characteristic length for Na (neurons)
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_33" name="k_pump" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_33">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T16:35:13Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          transport rate constant
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_34" name="K_m,Na-pump" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_34">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T11:23:12Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_35" name="K_m_G6P_GLYS" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_35">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T11:46:55Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_36" name="delta_GLY" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_36">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T11:01:28Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_37" name="K_m_GLY" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_37">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T17:31:27Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          affinity constant for GLY
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_38" name="g_Na_neurons" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_38">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:07:23Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          surface resistance neurons
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_39" name="g_Na_astrocytes" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_39">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:08:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          surface resistance astrocytes
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_40" name="Vm" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_40">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:09:02Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          surface potential
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_41" name="RT" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_41">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:09:56Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          perfect gas constant and temperature kPa
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_42" name="F" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_42">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:11:14Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          molecular charge
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_43" name="vn_1" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_43">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:56:47Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          base sodium transport rate during stimulation (tail pinch)
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_44" name="vn_2" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_44">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:57:36Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          early stimulation of sodium transport rate (tail pinch)
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_45" name="t_stim_tp" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_45">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T11:28:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          time constant of stimulation (tail pinch)
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_46" name="is_stimulated" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_46">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T11:22:12Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          if(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time> le 200 or &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time> ge &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_0],Reference=Value>+&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_end],Reference=Value>,0,1)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_47" name="v_stim" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_47">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T11:35:31Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[is_stimulated],Reference=Value>*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[stimulus],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_48" name="R_Na_GLU" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_48">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T08:55:11Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          Glutamate / Na ratio for release of GLU by neurons during stimulation
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_49" name="V_eg_max_GLU" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_49">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T08:59:57Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          maximum uptake rate of GLU in astrocytes
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_50" name="K_m_GLU" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_50">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-09T12:39:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          <body xmlns="http://www.w3.org/1999/xhtml">
    <pre>Affinity constant for GLU</pre>
  </body>
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_51" name="V_gn_max_GLU" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_51">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T09:09:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          reaction rate constant for glutamine synthase
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_52" name="delta_HK" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_52">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2015-09-28T10:55:53Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_53" name="BOLD signal" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_53">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-24T12:03:01Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>*(7*(1-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[dHb],Reference=Concentration>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[dHb],Reference=InitialConcentration>))+2*((1-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[dHb],Reference=Concentration>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[dHb],Reference=InitialConcentration>)/(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>))+(2*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[E0],Reference=InitialValue>-0.2)*(1-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=InitialVolume>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_54" name="E0" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_54">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-24T12:11:37Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          1-(2*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[O2],Reference=InitialConcentration>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[O2],Reference=InitialConcentration>)/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[O2],Reference=Concentration>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_55" name="K_m_ATP(ATPase)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_55">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:10:35Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_56" name="NULL" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_56">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T16:37:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_57" name="Vmax_ne_LAC (wrt extracellular space)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_57">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:31:33Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_58" name="PS_cap_astrocytes (wrt capillaries)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_58">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:33:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          O2 mass transfer constant between capillaries and astrocytes
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_59" name="PS_cap_neuron (wrt capillaries)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_59">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:32:34Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          O2 mass transfer constant between capillary and neuron
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_60" name="Vmax_eg_GLU (wrt extracellular space)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_60">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T09:09:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          reaction rate constant for glutamine synthase
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_61" name="K_T_GLC_ce (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_61">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:37:57Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_62" name="Vmax_ce_GLC (wrt capillaries) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_62">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:35:28Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          0.118 from Aubert 2005 model
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ce_GLC (Aubert)],Reference=InitialValue>*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=Volume>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_63" name="Vmax_eg_GLC (wrt astrocytes) (Aubert) " simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_63">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:33:08Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          1020 from model Aubert 2005
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_eg_GLC (Aubert) ],Reference=Value>*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Reference=Volume>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_64" name="K_T_GLC_eg (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_64">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:41:02Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_65" name="K_T_GLC_en (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_65">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:39:37Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_66" name="Vmax_en_GLC (wrt neurons) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_66">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-10-11T13:24:04Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_en_GLC  (Aubert)],Reference=InitialValue>*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Reference=Volume>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_67" name="K_T_GLC_cg (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_67">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:38:51Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_68" name="Vmax_cg_GLC (wrt capillaries) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_68">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:34:06Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          0.0093 from Aubert 2005 model
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_cg_GLC  (Aubert)],Reference=InitialValue>*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=Volume>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_69" name="Vmax_ec_LAC (wrt extracellular space) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_69">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:26:56Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.00783*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_70" name="Vmax_gc_LAC (wrt astrocytes) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_70">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:28:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.0058*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_71" name="Vmax_ge_LAC (wrt astrocytes) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_71">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:29:09Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.076*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_72" name="Vmax_ne_LAC (wrt neurons) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_72">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-11T10:31:33Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          0.29*&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=Value>
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_73" name="K_T_LAC_ne (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_73">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T12:03:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_74" name="K_T_LAC_ge (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_74">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-10T12:02:53Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_75" name="K_T_LAC_gc (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_75">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:24:14Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_76" name="K_T_LAC_ec (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_76">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:24:13Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_77" name="PS_cap_astrocytes (wrt capillaries) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_77">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:33:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          0.414 from Aubert 2005
0.25 is the astrocytic volume considered in Aubert 2005
        </Comment>
        <Expression>
          0.414*0.25*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=Volume>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_78" name="PS_cap_neuron (wrt capillaries) (Aubert)" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_78">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:32:34Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          1.73 from model Aubert 2005
0.45 is the neuronal volume considered in Aubert 2005
        </Comment>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[_PScap],Reference=Value>*0.45*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Reference=Volume>/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=Volume>)
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_79" name="K_O2 (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_79">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:29:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          0.0361 taken from model Aubert 2005
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_80" name="nh_O2 (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_80">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:42:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          2.73 taken from Aubert 2005
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_81" name="Vmax_f_PGI (Cloutier)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_81">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-10-29T13:54:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          from Model Cloutier 2009
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_82" name="Vmax_r_PGI (Cloutier)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_82">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-10-29T13:54:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_83" name="Vmax_ce_GLC (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_83">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:35:28Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_84" name="Vmax_cg_GLC  (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_84">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:34:06Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_85" name="Vmax_eg_GLC (Aubert) " simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_85">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:33:08Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_86" name="Vmax_en_GLC  (Aubert)" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_86">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-10-11T13:24:04Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_87" name="sf" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_87">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-12-12T16:27:31Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Comment>
          scaling factor transport
        </Comment>
      </ModelValue>
      <ModelValue key="ModelValue_88" name="_PScap" simulationType="fixed">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_88">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2016-12-12T15:30:45Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
      </ModelValue>
      <ModelValue key="ModelValue_89" name="f_CBF_dyn" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_89">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2017-02-26T15:09:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          1+&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[delta_F],Reference=Value>*(1/(1+exp(-4.59186*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time>-(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_0],Reference=Value>+&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_1],Reference=Value>-3))))-1/(1+exp(-4.59186*(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time>-(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_0],Reference=Value>+&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_1],Reference=Value>+&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_end],Reference=Value>+3)))))
        </Expression>
      </ModelValue>
      <ModelValue key="ModelValue_90" name="stimulus" simulationType="assignment">
        <MiriamAnnotation>
<rdf:RDF xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#ModelValue_90">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2017-02-26T15:12:10Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <Expression>
          &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[vn_1],Reference=InitialValue>+&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[vn_2],Reference=Value>*((&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_0],Reference=Value>)/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_stim_tp],Reference=Value>)*exp(-(&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_0],Reference=Value>)/&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_stim_tp],Reference=Value>)
        </Expression>
      </ModelValue>
    </ListOfModelValues>
    <ListOfReactions>
      <Reaction key="Reaction_0" name="HK_astrocytes (R01786)  (HeinrichSchuster)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_0">
    <dcterms:bibliographicCitation>
      <rdf:Description>
        <CopasiMT:isDescribedBy rdf:resource="urn:miriam:isbn:978-1-4612-8492-5"/>
      </rdf:Description>
    </dcterms:bibliographicCitation>
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:12:26Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01786"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_65" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_63" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_67" stoichiometry="1"/>
          <Product metabolite="Metabolite_89" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfModifiers>
          <Modifier metabolite="Metabolite_67" stoichiometry="1"/>
        </ListOfModifiers>
        <ListOfConstants>
          <Constant key="Parameter_5043" name="k_HK" value="0.01"/>
          <Constant key="Parameter_5044" name="ATP" value="2.7333e+020"/>
          <Constant key="Parameter_5045" name="K_I_G6P" value="0.02"/>
        </ListOfConstants>
        <KineticLaw function="Function_57" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_354">
              <SourceParameter reference="Parameter_5043"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_335">
              <SourceParameter reference="Metabolite_65"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_345">
              <SourceParameter reference="Metabolite_67"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_355">
              <SourceParameter reference="Parameter_5045"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_1" name="PFK_neurons (R04779, R01070, R01015)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_1">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T12:15:40Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01015"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01070"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R04779"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000395"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_17" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_15" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_19" stoichiometry="2"/>
          <Product metabolite="Metabolite_37" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5046" name="k_PFK" value="0.44"/>
          <Constant key="Parameter_5047" name="ATP" value="6.12793e+020"/>
          <Constant key="Parameter_5048" name="K_I_ATP" value="1"/>
          <Constant key="Parameter_5049" name="nH" value="4"/>
          <Constant key="Parameter_5050" name="K_m_F6P" value="0.18"/>
        </ListOfConstants>
        <KineticLaw function="Function_45" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_281">
              <SourceParameter reference="Parameter_5046"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_277">
              <SourceParameter reference="Metabolite_15"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_272">
              <SourceParameter reference="ModelValue_4"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_283">
              <SourceParameter reference="ModelValue_5"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_285">
              <SourceParameter reference="Metabolite_17"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_287">
              <SourceParameter reference="Parameter_5050"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_2" name="PFK_astrocytes (R04779, R01070, R01015)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_2">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T12:14:55Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01015"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01070"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R04779"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000395"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_69" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_65" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_71" stoichiometry="2"/>
          <Product metabolite="Metabolite_89" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5038" name="k_PFK" value="0.2"/>
          <Constant key="Parameter_5039" name="ATP" value="2.7333e+020"/>
          <Constant key="Parameter_5040" name="K_I_ATP" value="1"/>
          <Constant key="Parameter_5041" name="nH" value="4"/>
          <Constant key="Parameter_5042" name="K_m_F6P" value="0.18"/>
        </ListOfConstants>
        <KineticLaw function="Function_45" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_281">
              <SourceParameter reference="Parameter_5038"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_277">
              <SourceParameter reference="Metabolite_65"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_272">
              <SourceParameter reference="ModelValue_4"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_283">
              <SourceParameter reference="ModelValue_5"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_285">
              <SourceParameter reference="Metabolite_69"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_287">
              <SourceParameter reference="Parameter_5042"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_3" name="PGK_neurons (R01061, R01512, R01518, R00658)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_3">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:12:40Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R00658"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01061"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01512"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01518"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000395"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_19" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_37" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_43" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_21" stoichiometry="1"/>
          <Product metabolite="Metabolite_23" stoichiometry="1"/>
          <Product metabolite="Metabolite_15" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5012" name="k_PGK" value="10"/>
          <Constant key="Parameter_5013" name="ADP" value="3.02805e+019"/>
          <Constant key="Parameter_5014" name="NAD" value="5.51847e+019"/>
        </ListOfConstants>
        <KineticLaw function="Function_44" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_274">
              <SourceParameter reference="Parameter_5012"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_269">
              <SourceParameter reference="Metabolite_19"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_276">
              <SourceParameter reference="Metabolite_37"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_278">
              <SourceParameter reference="Metabolite_43"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_280">
              <SourceParameter reference="Metabolite_21"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_4" name="PGK_astrocytes (R01061, R01512, R01518, R00658)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_4">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-12T10:20:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R00658"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01061"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01512"/>
    <CopasiMT:hasPart rdf:resource="urn:miriam:kegg.reaction:R01518"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000395"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_71" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_89" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_95" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_73" stoichiometry="1"/>
          <Product metabolite="Metabolite_75" stoichiometry="1"/>
          <Product metabolite="Metabolite_65" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5015" name="k_PGK" value="3"/>
          <Constant key="Parameter_5016" name="ADP" value="6.69902e+019"/>
          <Constant key="Parameter_5017" name="NAD" value="2.43989e+019"/>
        </ListOfConstants>
        <KineticLaw function="Function_44" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_274">
              <SourceParameter reference="Parameter_5015"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_269">
              <SourceParameter reference="Metabolite_71"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_276">
              <SourceParameter reference="Metabolite_89"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_278">
              <SourceParameter reference="Metabolite_95"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_280">
              <SourceParameter reference="Metabolite_73"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_5" name="PK_neurons (R00200)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_5">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:08:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R00200" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_23" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_37" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_15" stoichiometry="1"/>
          <Product metabolite="Metabolite_25" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5018" name="k_PK" value="44"/>
          <Constant key="Parameter_5019" name="ADP" value="3.02805e+019"/>
        </ListOfConstants>
        <KineticLaw function="Function_43" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_271">
              <SourceParameter reference="Parameter_5018"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_266">
              <SourceParameter reference="Metabolite_23"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_273">
              <SourceParameter reference="Metabolite_37"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_6" name="PK_astrocytes (R00200)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_6">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:09:09Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R00200"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_75" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_89" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_65" stoichiometry="1"/>
          <Product metabolite="Metabolite_77" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5020" name="k_PK" value="20"/>
          <Constant key="Parameter_5021" name="ADP" value="6.69902e+019"/>
        </ListOfConstants>
        <KineticLaw function="Function_43" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_271">
              <SourceParameter reference="Parameter_5020"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_266">
              <SourceParameter reference="Metabolite_75"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_273">
              <SourceParameter reference="Metabolite_89"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_7" name="mitochondrial_respiration_neurons (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_7">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T12:46:55Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0009060" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000395" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_25" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_29" stoichiometry="3"/>
          <Substrate metabolite="Metabolite_21" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_37" stoichiometry="15"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_15" stoichiometry="15"/>
          <Product metabolite="Metabolite_3" stoichiometry="3"/>
          <Product metabolite="Metabolite_43" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5022" name="v_max_mito" value="0.1"/>
          <Constant key="Parameter_5023" name="K_m_PYR" value="0.0632"/>
          <Constant key="Parameter_5024" name="ADP" value="3.02805e+019"/>
          <Constant key="Parameter_5025" name="K_m_ADP" value="0.00107"/>
          <Constant key="Parameter_5026" name="K_m_O2" value="0.0029658"/>
          <Constant key="Parameter_5027" name="ATP" value="6.12793e+020"/>
          <Constant key="Parameter_5028" name="alpha" value="5"/>
          <Constant key="Parameter_5029" name="beta" value="20"/>
        </ListOfConstants>
        <KineticLaw function="Function_59" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_364">
              <SourceParameter reference="Parameter_5022"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_360">
              <SourceParameter reference="Metabolite_25"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_352">
              <SourceParameter reference="ModelValue_15"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_365">
              <SourceParameter reference="Metabolite_37"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_367">
              <SourceParameter reference="ModelValue_13"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_369">
              <SourceParameter reference="Metabolite_29"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_371">
              <SourceParameter reference="ModelValue_14"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_373">
              <SourceParameter reference="Parameter_5028"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_375">
              <SourceParameter reference="Metabolite_15"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_377">
              <SourceParameter reference="Parameter_5029"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_379">
              <SourceParameter reference="Compartment_3"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_8" name="mitochondrial_respiration_astrocytes (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_8">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:03:10Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0009060"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000395"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_77" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_89" stoichiometry="15"/>
          <Substrate metabolite="Metabolite_81" stoichiometry="3"/>
          <Substrate metabolite="Metabolite_73" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_65" stoichiometry="15"/>
          <Product metabolite="Metabolite_3" stoichiometry="3"/>
          <Product metabolite="Metabolite_95" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5030" name="v_max_mito" value="0.01"/>
          <Constant key="Parameter_5031" name="K_m_PYR" value="0.0632"/>
          <Constant key="Parameter_5032" name="ADP" value="6.69902e+019"/>
          <Constant key="Parameter_5033" name="K_m_ADP" value="0.00107"/>
          <Constant key="Parameter_5034" name="K_m_O2" value="0.0029658"/>
          <Constant key="Parameter_5035" name="ATP" value="2.7333e+020"/>
          <Constant key="Parameter_5036" name="alpha" value="5"/>
          <Constant key="Parameter_5037" name="beta" value="20"/>
        </ListOfConstants>
        <KineticLaw function="Function_59" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_364">
              <SourceParameter reference="Parameter_5030"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_360">
              <SourceParameter reference="Metabolite_77"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_352">
              <SourceParameter reference="ModelValue_15"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_365">
              <SourceParameter reference="Metabolite_89"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_367">
              <SourceParameter reference="ModelValue_13"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_369">
              <SourceParameter reference="Metabolite_81"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_371">
              <SourceParameter reference="ModelValue_14"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_373">
              <SourceParameter reference="Parameter_5036"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_375">
              <SourceParameter reference="Metabolite_65"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_377">
              <SourceParameter reference="Parameter_5037"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_379">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_9" name="GLC_exchange_extracellular_space_neurons (Aubert)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_9">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T10:23:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015758"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_115" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_11" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5051" name="Vmax" value="11767.5"/>
          <Constant key="Parameter_5052" name="K" value="9"/>
          <Constant key="Parameter_5053" name="sf" value="0.75"/>
        </ListOfConstants>
        <KineticLaw function="Function_58" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_356">
              <SourceParameter reference="ModelValue_66"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_328">
              <SourceParameter reference="ModelValue_87"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_357">
              <SourceParameter reference="Metabolite_115"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_359">
              <SourceParameter reference="ModelValue_65"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_361">
              <SourceParameter reference="Metabolite_11"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_363">
              <SourceParameter reference="Compartment_7"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_10" name="GLC_exchange_extracellular_space_astrocytes (Aubert)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_10">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-10-22T11:31:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015758"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_115" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_63" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5054" name="Vmax" value="1275"/>
          <Constant key="Parameter_5055" name="K" value="9"/>
          <Constant key="Parameter_5056" name="sf" value="0.75"/>
        </ListOfConstants>
        <KineticLaw function="Function_58" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_356">
              <SourceParameter reference="ModelValue_63"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_328">
              <SourceParameter reference="ModelValue_87"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_357">
              <SourceParameter reference="Metabolite_115"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_359">
              <SourceParameter reference="ModelValue_64"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_361">
              <SourceParameter reference="Metabolite_63"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_363">
              <SourceParameter reference="Compartment_7"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_11" name="GLC_exchange_capillary_ec (Aubert)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_11">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:07:40Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015758"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_5" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_115" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5057" name="Vmax" value="4.29091"/>
          <Constant key="Parameter_5058" name="K" value="9"/>
          <Constant key="Parameter_5059" name="sf" value="0.75"/>
        </ListOfConstants>
        <KineticLaw function="Function_58" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_356">
              <SourceParameter reference="ModelValue_62"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_328">
              <SourceParameter reference="ModelValue_87"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_357">
              <SourceParameter reference="Metabolite_5"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_359">
              <SourceParameter reference="ModelValue_61"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_361">
              <SourceParameter reference="Metabolite_115"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_363">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_12" name="GLC_exchange_capillary_astrocytes (Aubert)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_12">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:21:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015758"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_5" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_63" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5060" name="Vmax" value="0.422727"/>
          <Constant key="Parameter_5061" name="K" value="9"/>
          <Constant key="Parameter_5062" name="sf" value="0.75"/>
        </ListOfConstants>
        <KineticLaw function="Function_58" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_356">
              <SourceParameter reference="ModelValue_68"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_328">
              <SourceParameter reference="ModelValue_87"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_357">
              <SourceParameter reference="Metabolite_5"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_359">
              <SourceParameter reference="ModelValue_67"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_361">
              <SourceParameter reference="Metabolite_63"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_363">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_13" name="LAC_exchange_ec_capillary" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_13">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:36:06Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015727"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_117" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_7" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5063" name="Vmax" value="0.0058725"/>
          <Constant key="Parameter_5064" name="K" value="0.5"/>
        </ListOfConstants>
        <KineticLaw function="Function_46" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_288">
              <SourceParameter reference="ModelValue_69"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_284">
              <SourceParameter reference="Metabolite_117"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_275">
              <SourceParameter reference="ModelValue_76"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_289">
              <SourceParameter reference="Metabolite_7"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_291">
              <SourceParameter reference="Compartment_7"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_14" name="LAC_exchange_neurons_ec" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_14">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:37:35Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015727"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_117" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_27" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5065" name="Vmax" value="0.2175"/>
          <Constant key="Parameter_5066" name="K" value="0.5"/>
        </ListOfConstants>
        <KineticLaw function="Function_46" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_288">
              <SourceParameter reference="ModelValue_72"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_284">
              <SourceParameter reference="Metabolite_117"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_275">
              <SourceParameter reference="ModelValue_73"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_289">
              <SourceParameter reference="Metabolite_27"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_291">
              <SourceParameter reference="Compartment_7"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_15" name="LAC_exchange_astrocytes_ec" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_15">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:40:22Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015727"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_79" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_117" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5067" name="Vmax" value="0.057"/>
          <Constant key="Parameter_5068" name="K" value="0.5"/>
        </ListOfConstants>
        <KineticLaw function="Function_46" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_288">
              <SourceParameter reference="ModelValue_71"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_284">
              <SourceParameter reference="Metabolite_79"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_275">
              <SourceParameter reference="ModelValue_74"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_289">
              <SourceParameter reference="Metabolite_117"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_291">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_16" name="LAC_exchange_astrocytes_capillary" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_16">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-11-07T12:41:33Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015727"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_79" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_7" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5069" name="Vmax" value="0.00435"/>
          <Constant key="Parameter_5070" name="K" value="0.5"/>
        </ListOfConstants>
        <KineticLaw function="Function_46" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_288">
              <SourceParameter reference="ModelValue_70"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_284">
              <SourceParameter reference="Metabolite_79"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_275">
              <SourceParameter reference="ModelValue_75"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_289">
              <SourceParameter reference="Metabolite_7"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_291">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_17" name="O2_exchange_capillary_neurons" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_17">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-19T11:47:23Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015671"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Comment>
          O2 exchange between capillary and neurons
        </Comment>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_1" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_29" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5071" name="PScap" value="40.5"/>
          <Constant key="Parameter_5072" name="Volume" value="0.45"/>
          <Constant key="Parameter_5073" name="KO2" value="0.0361"/>
          <Constant key="Parameter_5074" name="HbOP" value="8.6"/>
          <Constant key="Parameter_5075" name="nh" value="2.73"/>
        </ListOfConstants>
        <KineticLaw function="Function_54" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_330">
              <SourceParameter reference="ModelValue_78"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_322">
              <SourceParameter reference="Compartment_3"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_305">
              <SourceParameter reference="ModelValue_79"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_332">
              <SourceParameter reference="ModelValue_30"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_334">
              <SourceParameter reference="Metabolite_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_336">
              <SourceParameter reference="ModelValue_80"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_338">
              <SourceParameter reference="Metabolite_29"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_340">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_18" name="O2_exchange_capillary_astrocytes" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_18">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T11:17:39Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015671"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_1" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_81" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5076" name="PScap" value="10"/>
          <Constant key="Parameter_5077" name="Volume" value="0.25"/>
          <Constant key="Parameter_5078" name="KO2" value="0.0361"/>
          <Constant key="Parameter_5079" name="HbOP" value="8.6"/>
          <Constant key="Parameter_5080" name="nh" value="2.73"/>
        </ListOfConstants>
        <KineticLaw function="Function_54" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_330">
              <SourceParameter reference="Parameter_5076"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_322">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_305">
              <SourceParameter reference="ModelValue_79"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_332">
              <SourceParameter reference="ModelValue_30"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_334">
              <SourceParameter reference="Metabolite_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_336">
              <SourceParameter reference="ModelValue_80"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_338">
              <SourceParameter reference="Metabolite_81"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_340">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_19" name="O2_exchange_artery_capillary" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_19">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T11:42:10Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015671"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_123" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_1" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5081" name="F_in" value="0.012"/>
          <Constant key="Parameter_5082" name="V_c" value="0.0055"/>
        </ListOfConstants>
        <KineticLaw function="Function_53" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_326">
              <SourceParameter reference="ModelValue_27"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_310">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_329">
              <SourceParameter reference="Compartment_11"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_318">
              <SourceParameter reference="Metabolite_123"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_327">
              <SourceParameter reference="Metabolite_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_20" name="GLC_exchange_artery_capillary" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_20">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T11:44:42Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015758"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_127" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_5" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5083" name="F_in" value="0.012"/>
          <Constant key="Parameter_5089" name="V_c" value="0.0055"/>
        </ListOfConstants>
        <KineticLaw function="Function_53" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_326">
              <SourceParameter reference="ModelValue_27"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_310">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_329">
              <SourceParameter reference="Compartment_11"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_318">
              <SourceParameter reference="Metabolite_127"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_327">
              <SourceParameter reference="Metabolite_5"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_21" name="LAC_exchange_capillary_artery" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_21">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T11:45:55Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015727"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_7" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_129" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5088" name="F_in" value="0.012"/>
          <Constant key="Parameter_5087" name="V_c" value="0.0055"/>
        </ListOfConstants>
        <KineticLaw function="Function_53" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_326">
              <SourceParameter reference="ModelValue_27"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_310">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_329">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_318">
              <SourceParameter reference="Metabolite_7"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_327">
              <SourceParameter reference="Metabolite_129"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_22" name="CO2_exchange_capillary_artery" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_22">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-20T12:43:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0006810"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_3" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_125" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5084" name="F_in" value="0.012"/>
          <Constant key="Parameter_5085" name="V_c" value="0.0055"/>
        </ListOfConstants>
        <KineticLaw function="Function_53" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_326">
              <SourceParameter reference="ModelValue_27"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_310">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_329">
              <SourceParameter reference="Compartment_1"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_318">
              <SourceParameter reference="Metabolite_3"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_327">
              <SourceParameter reference="Metabolite_125"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_23" name="Na+_exchange_neurons_extracellular_space (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_23">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T16:39:18Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0036376"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_15" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_33" stoichiometry="3"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_37" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5086" name="Sm" value="40500"/>
          <Constant key="Parameter_5090" name="k_pump" value="3.17e-007"/>
          <Constant key="Parameter_5091" name="Km_pump" value="0.4243"/>
          <Constant key="Parameter_5094" name="Volume" value="0.45"/>
        </ListOfConstants>
        <KineticLaw function="Function_56" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_351">
              <SourceParameter reference="ModelValue_32"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_347">
              <SourceParameter reference="Compartment_3"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_343">
              <SourceParameter reference="ModelValue_33"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_331">
              <SourceParameter reference="Metabolite_15"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_339">
              <SourceParameter reference="Metabolite_33"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_353">
              <SourceParameter reference="ModelValue_34"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_24" name="Na+_exchange_astrocytes_extracellular_space (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_24">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-21T16:39:18Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0036376"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_65" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_85" stoichiometry="3"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_89" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5093" name="Sm" value="10500"/>
          <Constant key="Parameter_5092" name="k_pump" value="3.17e-007"/>
          <Constant key="Parameter_5095" name="Km_pump" value="0.4243"/>
          <Constant key="Parameter_5096" name="Volume" value="0.25"/>
        </ListOfConstants>
        <KineticLaw function="Function_56" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_351">
              <SourceParameter reference="ModelValue_31"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_347">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_343">
              <SourceParameter reference="ModelValue_33"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_331">
              <SourceParameter reference="Metabolite_65"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_339">
              <SourceParameter reference="Metabolite_85"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_353">
              <SourceParameter reference="ModelValue_34"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_25" name="LEAK_Na_neurons (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_25">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:28:58Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:1990118"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_121" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_33" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5097" name="Sm" value="40500"/>
          <Constant key="Parameter_5098" name="gNA" value="0.0039"/>
          <Constant key="Parameter_5099" name="F" value="96500"/>
          <Constant key="Parameter_5100" name="RT" value="2.57734e+006"/>
          <Constant key="Parameter_5101" name="Vm" value="-70"/>
          <Constant key="Parameter_5102" name="Volume" value="0.45"/>
        </ListOfConstants>
        <KineticLaw function="Function_55" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_341">
              <SourceParameter reference="ModelValue_32"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_337">
              <SourceParameter reference="ModelValue_38"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_333">
              <SourceParameter reference="Compartment_3"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_314">
              <SourceParameter reference="ModelValue_42"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_342">
              <SourceParameter reference="ModelValue_41"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_344">
              <SourceParameter reference="Metabolite_121"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_346">
              <SourceParameter reference="Metabolite_33"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_348">
              <SourceParameter reference="ModelValue_40"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_350">
              <SourceParameter reference="Compartment_3"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_26" name="LEAK_Na_astrocytes (n.a)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_26">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T10:28:58Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:1990118"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_121" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_85" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5103" name="Sm" value="10500"/>
          <Constant key="Parameter_5104" name="gNA" value="0.0039"/>
          <Constant key="Parameter_5105" name="F" value="96500"/>
          <Constant key="Parameter_5106" name="RT" value="2.57734e+006"/>
          <Constant key="Parameter_5107" name="Vm" value="-70"/>
          <Constant key="Parameter_5120" name="Volume" value="0.25"/>
        </ListOfConstants>
        <KineticLaw function="Function_55" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_341">
              <SourceParameter reference="ModelValue_31"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_337">
              <SourceParameter reference="Parameter_5104"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_333">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_314">
              <SourceParameter reference="ModelValue_42"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_342">
              <SourceParameter reference="ModelValue_41"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_344">
              <SourceParameter reference="Metabolite_121"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_346">
              <SourceParameter reference="Metabolite_85"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_348">
              <SourceParameter reference="ModelValue_40"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_350">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_27" name="Na+_exchange_extracellular_space_neurons  (stimulation)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_27">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-26T11:38:49Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:1990118"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_121" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_33" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5121" name="vstim" value="-0"/>
        </ListOfConstants>
        <KineticLaw function="Function_50" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_307">
              <SourceParameter reference="ModelValue_47"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_293">
              <SourceParameter reference="Compartment_3"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_28" name="GLU_exchange_neurons_extracellular_space" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_28">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T08:56:50Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015813"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <Comment>
          Glutamate release by neurons to extracellular space
        </Comment>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_35" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_119" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5122" name="vSTIM" value="-0"/>
          <Constant key="Parameter_5123" name="ratio_Na_GLU" value="0.075"/>
          <Constant key="Parameter_5124" name="Km_GLU" value="0.05"/>
        </ListOfConstants>
        <KineticLaw function="Function_49" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_303">
              <SourceParameter reference="ModelValue_47"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_299">
              <SourceParameter reference="ModelValue_48"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_290">
              <SourceParameter reference="Metabolite_35"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_304">
              <SourceParameter reference="ModelValue_50"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_306">
              <SourceParameter reference="Compartment_3"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_29" name="GLU_exchange_extracellular_space_astrocytes" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_29">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T09:05:18Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015813"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_119" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_87" stoichiometry="1"/>
          <Product metabolite="Metabolite_85" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5125" name="Vmax_GLU" value="0.026"/>
          <Constant key="Parameter_5126" name="K_m_GLU" value="0.05"/>
        </ListOfConstants>
        <KineticLaw function="Function_47" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_292">
              <SourceParameter reference="ModelValue_60"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_279">
              <SourceParameter reference="Metabolite_119"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_286">
              <SourceParameter reference="ModelValue_50"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_294">
              <SourceParameter reference="Compartment_7"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_30" name="GLU_exchange_astrocytes_neurons" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_30">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-05-27T09:14:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:go:GO:0015813"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000185"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_87" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_65" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_35" stoichiometry="1"/>
          <Product metabolite="Metabolite_89" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5127" name="Vmax_GLU" value="0.3"/>
          <Constant key="Parameter_5128" name="K_m_GLU" value="0.05"/>
          <Constant key="Parameter_5129" name="K_m_ATP" value="0.01532"/>
        </ListOfConstants>
        <KineticLaw function="Function_48" unitType="Default">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_295">
              <SourceParameter reference="ModelValue_51"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_282">
              <SourceParameter reference="Metabolite_87"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_296">
              <SourceParameter reference="ModelValue_50"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_298">
              <SourceParameter reference="Metabolite_65"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_300">
              <SourceParameter reference="ModelValue_12"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_302">
              <SourceParameter reference="Compartment_5"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_31" name="inflow of dHb" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_31">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-24T11:35:38Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfProducts>
          <Product metabolite="Metabolite_9" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfModifiers>
          <Modifier metabolite="Metabolite_123" stoichiometry="1"/>
          <Modifier metabolite="Metabolite_1" stoichiometry="1"/>
        </ListOfModifiers>
        <ListOfConstants>
          <Constant key="Parameter_5130" name="F_in" value="0.012"/>
        </ListOfConstants>
        <KineticLaw function="Function_40" unitType="ConcentrationPerTime" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_264">
              <SourceParameter reference="ModelValue_27"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_254">
              <SourceParameter reference="Metabolite_123"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_258">
              <SourceParameter reference="Metabolite_1"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_32" name="outflow of dHb" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_32">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-24T11:42:03Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_9" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfConstants>
          <Constant key="Parameter_5131" name="F_out" value="0.012"/>
        </ListOfConstants>
        <KineticLaw function="Function_41" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_265">
              <SourceParameter reference="ModelValue_28"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_262">
              <SourceParameter reference="Metabolite_9"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_267">
              <SourceParameter reference="Compartment_9"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_33" name="ATPase_neurons (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_33">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:11:30Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_15" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_37" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5132" name="VmaxATPase" value="0.07"/>
          <Constant key="Parameter_5133" name="Km_ATP" value="0.001"/>
        </ListOfConstants>
        <KineticLaw function="Function_42" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_268">
              <SourceParameter reference="Parameter_5132"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_246">
              <SourceParameter reference="Metabolite_15"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_270">
              <SourceParameter reference="ModelValue_55"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_34" name="ATPase_astrocytes (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_34">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:13:59Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_65" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_89" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5134" name="VmaxATPase" value="0.035"/>
          <Constant key="Parameter_5135" name="Km_ATP" value="0.001"/>
        </ListOfConstants>
        <KineticLaw function="Function_42" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_268">
              <SourceParameter reference="Parameter_5134"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_246">
              <SourceParameter reference="Metabolite_65"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_270">
              <SourceParameter reference="ModelValue_55"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_35" name="AK_neurons (R00127)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_35">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:48:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R00127"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_37" stoichiometry="2"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_15" stoichiometry="1"/>
          <Product metabolite="Metabolite_39" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5136" name="k1" value="1000"/>
          <Constant key="Parameter_5137" name="k2" value="920"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_5136"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_37"/>
              <SourceParameter reference="Metabolite_37"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_5137"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_15"/>
              <SourceParameter reference="Metabolite_39"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_36" name="AK_astrocytes (R00127)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_36">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-10-22T11:48:24Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R00127"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_89" stoichiometry="2"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_65" stoichiometry="1"/>
          <Product metabolite="Metabolite_91" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5138" name="k1" value="1000"/>
          <Constant key="Parameter_5139" name="k2" value="920"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_5138"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_89"/>
              <SourceParameter reference="Metabolite_89"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_5139"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_65"/>
              <SourceParameter reference="Metabolite_91"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_37" name="CK_astrocytes (R01881)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_37">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:57:05Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01881"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_89" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_83" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_65" stoichiometry="1"/>
          <Product metabolite="Metabolite_93" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4998" name="k1" value="0.5"/>
          <Constant key="Parameter_4999" name="k2" value="0.01"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_4998"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_89"/>
              <SourceParameter reference="Metabolite_83"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_4999"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_65"/>
              <SourceParameter reference="Metabolite_93"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_38" name="CK_neurons (R01881)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_38">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-16T13:54:02Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01881"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_31" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_37" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_15" stoichiometry="1"/>
          <Product metabolite="Metabolite_41" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5000" name="k1" value="0.5"/>
          <Constant key="Parameter_5001" name="k2" value="0.01"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_5000"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_31"/>
              <SourceParameter reference="Metabolite_37"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_5001"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_15"/>
              <SourceParameter reference="Metabolite_41"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_39" name="LDH_astrocytes (R00703)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_39">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:22:07Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R00703"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_77" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_73" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_79" stoichiometry="1"/>
          <Product metabolite="Metabolite_95" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5002" name="k1" value="780"/>
          <Constant key="Parameter_5003" name="k2" value="32"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_5002"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_77"/>
              <SourceParameter reference="Metabolite_73"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_5003"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_79"/>
              <SourceParameter reference="Metabolite_95"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_40" name="LDH_neurons (R00703)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_40">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2013-08-13T11:19:36Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R00703"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_25" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_21" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_27" stoichiometry="1"/>
          <Product metabolite="Metabolite_43" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5004" name="k1" value="2000"/>
          <Constant key="Parameter_5005" name="k2" value="15"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_5004"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_25"/>
              <SourceParameter reference="Metabolite_21"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_5005"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_27"/>
              <SourceParameter reference="Metabolite_43"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_41" name="ZWF_astrocytes (R02736)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_41">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T12:54:19Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R02736" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_67" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_111" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_97" stoichiometry="1"/>
          <Product metabolite="Metabolite_113" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5006" name="Vmax" value="0.29057"/>
          <Constant key="Parameter_5007" name="K_S1" value="6.91392e-005"/>
          <Constant key="Parameter_5008" name="K_S2" value="1.31616e-005"/>
          <Constant key="Parameter_5009" name="Keq" value="22906.4"/>
          <Constant key="Parameter_5010" name="K_P1" value="0.0180932"/>
          <Constant key="Parameter_5140" name="K_P2" value="0.00050314"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_5006"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_5007"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_5008"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_67"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_111"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_97"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_113"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_5009"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_5010"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_5140"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_42" name="ZWF_neurons (R02736)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_42">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T12:54:19Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R02736" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_13" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_61" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_45" stoichiometry="1"/>
          <Product metabolite="Metabolite_59" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_5141" name="Vmax" value="0.586458"/>
          <Constant key="Parameter_4865" name="K_S1" value="6.91392e-005"/>
          <Constant key="Parameter_4864" name="K_S2" value="1.31616e-005"/>
          <Constant key="Parameter_4863" name="Keq" value="22906.4"/>
          <Constant key="Parameter_4862" name="K_P1" value="0.0180932"/>
          <Constant key="Parameter_4861" name="K_P2" value="0.00050314"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_5141"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4865"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4864"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_13"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_61"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_45"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_59"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4863"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4862"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4861"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_43" name="SOL_neurons (R02035)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_43">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T12:58:22Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R02035"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_45" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_47" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4860" name="Vmax" value="0.372782"/>
          <Constant key="Parameter_4859" name="K_S1" value="0.0180932"/>
          <Constant key="Parameter_4858" name="Keq" value="531174"/>
          <Constant key="Parameter_4857" name="K_P1" value="2.28618"/>
        </ListOfConstants>
        <KineticLaw function="Function_52" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_324">
              <SourceParameter reference="Parameter_4860"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_320">
              <SourceParameter reference="Parameter_4859"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_316">
              <SourceParameter reference="Metabolite_45"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_312">
              <SourceParameter reference="Metabolite_47"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_308">
              <SourceParameter reference="Parameter_4858"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_325">
              <SourceParameter reference="Parameter_4857"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_44" name="SOL_astrocytes (R02035)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_44">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T12:58:22Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R02035" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_97" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_99" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4856" name="Vmax" value="0.184701"/>
          <Constant key="Parameter_4855" name="K_S1" value="0.0180932"/>
          <Constant key="Parameter_4854" name="Keq" value="531174"/>
          <Constant key="Parameter_4853" name="K_P1" value="2.28618"/>
        </ListOfConstants>
        <KineticLaw function="Function_52" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_324">
              <SourceParameter reference="Parameter_4856"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_320">
              <SourceParameter reference="Parameter_4855"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_316">
              <SourceParameter reference="Metabolite_97"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_312">
              <SourceParameter reference="Metabolite_99"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_308">
              <SourceParameter reference="Parameter_4854"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_325">
              <SourceParameter reference="Parameter_4853"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_45" name="GND_neurons  (R01528)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_45">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T13:01:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01528"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_47" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_61" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_49" stoichiometry="1"/>
          <Product metabolite="Metabolite_59" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4852" name="Vmax" value="2.65764"/>
          <Constant key="Parameter_4851" name="K_S1" value="3.23421e-005"/>
          <Constant key="Parameter_4850" name="K_S2" value="3.11043e-006"/>
          <Constant key="Parameter_4849" name="Keq" value="4.0852e+007"/>
          <Constant key="Parameter_4848" name="K_P1" value="0.0537179"/>
          <Constant key="Parameter_4847" name="K_P2" value="0.00050314"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4852"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4851"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4850"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_47"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_61"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_49"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_59"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4849"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4848"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4847"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_46" name="GND_astrocytes (R01528)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_46">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T13:01:00Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01528"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_99" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_111" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_101" stoichiometry="1"/>
          <Product metabolite="Metabolite_113" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4846" name="Vmax" value="1.31677"/>
          <Constant key="Parameter_4845" name="K_S1" value="3.23421e-005"/>
          <Constant key="Parameter_4844" name="K_S2" value="3.11043e-006"/>
          <Constant key="Parameter_4843" name="Keq" value="4.0852e+007"/>
          <Constant key="Parameter_4842" name="K_P1" value="0.00050314"/>
          <Constant key="Parameter_4841" name="K_P2" value="0.0537179"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4846"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4845"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4844"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_99"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_111"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_101"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_113"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4843"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4842"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4841"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_47" name="RPE_neurons (R01529)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_47">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T13:04:15Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01528"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_49" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_51" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4840" name="Vmax" value="0.0156605"/>
          <Constant key="Parameter_4839" name="K_S1" value="0.0537179"/>
          <Constant key="Parameter_4838" name="Keq" value="39.2574"/>
          <Constant key="Parameter_4837" name="K_P1" value="0.603002"/>
        </ListOfConstants>
        <KineticLaw function="Function_52" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_324">
              <SourceParameter reference="Parameter_4840"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_320">
              <SourceParameter reference="Parameter_4839"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_316">
              <SourceParameter reference="Metabolite_49"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_312">
              <SourceParameter reference="Metabolite_51"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_308">
              <SourceParameter reference="Parameter_4838"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_325">
              <SourceParameter reference="Parameter_4837"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_48" name="RPE_astrocytes (R01529)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_48">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-24T13:04:15Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01528"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_101" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_103" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4836" name="Vmax" value="0.00775925"/>
          <Constant key="Parameter_4835" name="K_S1" value="0.0537179"/>
          <Constant key="Parameter_4834" name="Keq" value="39.2574"/>
          <Constant key="Parameter_4833" name="K_P1" value="0.603002"/>
        </ListOfConstants>
        <KineticLaw function="Function_52" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_324">
              <SourceParameter reference="Parameter_4836"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_320">
              <SourceParameter reference="Parameter_4835"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_316">
              <SourceParameter reference="Metabolite_101"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_312">
              <SourceParameter reference="Metabolite_103"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_308">
              <SourceParameter reference="Parameter_4834"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_325">
              <SourceParameter reference="Parameter_4833"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_49" name="RKI_astrocytes (R01056)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_49">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T16:32:33Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01056"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_101" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_105" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4832" name="Vmax" value="0.000821984"/>
          <Constant key="Parameter_4831" name="K_S1" value="0.0537179"/>
          <Constant key="Parameter_4830" name="Keq" value="35.4534"/>
          <Constant key="Parameter_4829" name="K_P1" value="0.778461"/>
        </ListOfConstants>
        <KineticLaw function="Function_52" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_324">
              <SourceParameter reference="Parameter_4832"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_320">
              <SourceParameter reference="Parameter_4831"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_316">
              <SourceParameter reference="Metabolite_101"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_312">
              <SourceParameter reference="Metabolite_105"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_308">
              <SourceParameter reference="Parameter_4830"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_325">
              <SourceParameter reference="Parameter_4829"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_50" name="RKI_neurons (R01056)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_50">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T16:32:33Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01056"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_49" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_53" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4828" name="Vmax" value="0.00165901"/>
          <Constant key="Parameter_4827" name="K_S1" value="0.0537179"/>
          <Constant key="Parameter_4826" name="Keq" value="35.4534"/>
          <Constant key="Parameter_4825" name="K_P1" value="0.778461"/>
        </ListOfConstants>
        <KineticLaw function="Function_52" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_324">
              <SourceParameter reference="Parameter_4828"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_320">
              <SourceParameter reference="Parameter_4827"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_316">
              <SourceParameter reference="Metabolite_49"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_312">
              <SourceParameter reference="Metabolite_53"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_308">
              <SourceParameter reference="Parameter_4826"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_325">
              <SourceParameter reference="Parameter_4825"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_51" name="TKL-1_astrocytes (R01641)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_51">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T16:36:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01641" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_103" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_105" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_71" stoichiometry="1"/>
          <Product metabolite="Metabolite_107" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4824" name="Vmax" value="0.000244278"/>
          <Constant key="Parameter_4823" name="K_S1" value="0.000173625"/>
          <Constant key="Parameter_4822" name="K_S2" value="0.000585387"/>
          <Constant key="Parameter_4821" name="Keq" value="1.65287e+006"/>
          <Constant key="Parameter_4820" name="K_P1" value="0.168333"/>
          <Constant key="Parameter_4819" name="K_P2" value="0.192807"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4824"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4823"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4822"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_103"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_105"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_71"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_107"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4821"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4820"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4819"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_52" name="TKL-1_neurons (R01641)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_52">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T16:36:25Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01641" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_51" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_53" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_19" stoichiometry="1"/>
          <Product metabolite="Metabolite_55" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4818" name="Vmax" value="0.000493027"/>
          <Constant key="Parameter_4817" name="K_S1" value="0.000173625"/>
          <Constant key="Parameter_4816" name="K_S2" value="0.000585387"/>
          <Constant key="Parameter_4815" name="Keq" value="1.65287e+006"/>
          <Constant key="Parameter_4814" name="K_P1" value="0.168333"/>
          <Constant key="Parameter_4813" name="K_P2" value="0.192807"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4818"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4817"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4816"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_51"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_53"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_19"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_55"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4815"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4814"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4813"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_53" name="TAL_astrocytes (R01827)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_53">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T16:59:11Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01827"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_71" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_107" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_69" stoichiometry="1"/>
          <Product metabolite="Metabolite_109" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4812" name="Vmax" value="0.0080394"/>
          <Constant key="Parameter_4811" name="K_S1" value="0.168333"/>
          <Constant key="Parameter_4810" name="K_S2" value="0.192807"/>
          <Constant key="Parameter_4809" name="Keq" value="0.323922"/>
          <Constant key="Parameter_4808" name="K_P1" value="0.0799745"/>
          <Constant key="Parameter_4807" name="K_P2" value="0.109681"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4812"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4811"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4810"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_71"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_107"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_69"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_109"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4809"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4808"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4807"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_54" name="TAL_neurons (R01827)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_54">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T16:59:11Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01827" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_19" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_55" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_17" stoichiometry="1"/>
          <Product metabolite="Metabolite_57" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4806" name="Vmax" value="0.0162259"/>
          <Constant key="Parameter_4805" name="K_S1" value="0.168333"/>
          <Constant key="Parameter_4804" name="Keq" value="0.323922"/>
          <Constant key="Parameter_4803" name="K_P1" value="0.0799745"/>
          <Constant key="Parameter_4802" name="K_S2" value="0.192807"/>
          <Constant key="Parameter_4801" name="K_P2" value="0.109681"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4806"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4805"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4802"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_19"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_55"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_17"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_57"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4804"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4803"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4801"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_55" name="TKL-2_astrocytes (R01830)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_55">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T17:05:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01830" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_69" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_71" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_103" stoichiometry="1"/>
          <Product metabolite="Metabolite_109" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4800" name="Vmax" value="0.000137124"/>
          <Constant key="Parameter_4799" name="K_S1" value="0.0799745"/>
          <Constant key="Parameter_4798" name="K_S2" value="0.168333"/>
          <Constant key="Parameter_4797" name="Keq" value="0.0777764"/>
          <Constant key="Parameter_4796" name="K_P1" value="0.603002"/>
          <Constant key="Parameter_4795" name="K_P2" value="0.109681"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4800"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4799"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4798"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_69"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_71"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_103"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_109"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4797"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4796"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4795"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_56" name="TKL-2_neurons (R01830)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_56">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-26T17:05:46Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01830" />
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176" />
  </rdf:Description>
</rdf:RDF>
        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_17" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_19" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_51" stoichiometry="1"/>
          <Product metabolite="Metabolite_57" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4794" name="Vmax" value="0.000276758"/>
          <Constant key="Parameter_4793" name="K_S1" value="0.0799745"/>
          <Constant key="Parameter_4792" name="K_S2" value="0.168333"/>
          <Constant key="Parameter_4791" name="Keq" value="0.0777764"/>
          <Constant key="Parameter_4790" name="K_P1" value="0.603002"/>
          <Constant key="Parameter_4789" name="K_P2" value="0.109681"/>
        </ListOfConstants>
        <KineticLaw function="Function_51" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_297">
              <SourceParameter reference="Parameter_4794"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_301">
              <SourceParameter reference="Parameter_4793"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_309">
              <SourceParameter reference="Parameter_4792"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_311">
              <SourceParameter reference="Metabolite_17"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_313">
              <SourceParameter reference="Metabolite_19"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_315">
              <SourceParameter reference="Metabolite_51"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_317">
              <SourceParameter reference="Metabolite_57"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_319">
              <SourceParameter reference="Parameter_4791"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_321">
              <SourceParameter reference="Parameter_4790"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_323">
              <SourceParameter reference="Parameter_4789"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_57" name="NADPH oxidase neurons (R07172)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_57">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-28T09:25:30Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_59" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_61" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4788" name="k1" value="0.000423283"/>
        </ListOfConstants>
        <KineticLaw function="Function_13" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Parameter_4788"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_79">
              <SourceParameter reference="Metabolite_59"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_58" name="NADPH oxidase astrocytes (R07172)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_58">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-28T09:25:30Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_113" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_111" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4787" name="k1" value="0.000209722"/>
        </ListOfConstants>
        <KineticLaw function="Function_13" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Parameter_4787"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_79">
              <SourceParameter reference="Metabolite_113"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_59" name="R5P sink_astrocytes (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_59">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-28T09:28:18Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_105" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfConstants>
          <Constant key="Parameter_4786" name="k1" value="0"/>
        </ListOfConstants>
        <KineticLaw function="Function_13" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="ModelValue_56"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_79">
              <SourceParameter reference="Metabolite_105"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_60" name="R5P sink_neurons (n.a.)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_60">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-11-28T09:28:18Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000631"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_53" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfConstants>
          <Constant key="Parameter_4785" name="k1" value="0"/>
        </ListOfConstants>
        <KineticLaw function="Function_13" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_81">
              <SourceParameter reference="Parameter_4785"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_79">
              <SourceParameter reference="Metabolite_53"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_61" name="PGI_astrocytes (R02740) (HS)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_61">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-18T16:03:18Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R02740"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_67" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_69" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4784" name="k1" value="931.69"/>
          <Constant key="Parameter_4783" name="k2" value="2273.32"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_4784"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_67"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_4783"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_69"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_62" name="HK_neurons (R01786) (HeinrichSchuster)" reversible="false" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_62">
    <dcterms:bibliographicCitation>
      <rdf:Description>
        <CopasiMT:isDescribedBy rdf:resource="urn:miriam:isbn:978-1-4612-8492-5"/>
      </rdf:Description>
    </dcterms:bibliographicCitation>
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-04-09T16:12:38Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R01786"/>
    <CopasiMT:is rdf:resource="urn:miriam:sbo:SBO:0000176"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_15" stoichiometry="1"/>
          <Substrate metabolite="Metabolite_11" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_13" stoichiometry="1"/>
          <Product metabolite="Metabolite_37" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfModifiers>
          <Modifier metabolite="Metabolite_13" stoichiometry="1"/>
        </ListOfModifiers>
        <ListOfConstants>
          <Constant key="Parameter_4782" name="k_HK" value="0.022"/>
          <Constant key="Parameter_4781" name="K_I_G6P" value="0.02"/>
        </ListOfConstants>
        <KineticLaw function="Function_57" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_354">
              <SourceParameter reference="Parameter_4782"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_335">
              <SourceParameter reference="Metabolite_15"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_345">
              <SourceParameter reference="Metabolite_13"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_355">
              <SourceParameter reference="Parameter_4781"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
      <Reaction key="Reaction_63" name="PGI_neurons (R02740) (HS)" reversible="true" fast="false">
        <MiriamAnnotation>
<rdf:RDF
   xmlns:CopasiMT="http://www.copasi.org/RDF/MiriamTerms#"
   xmlns:dcterms="http://purl.org/dc/terms/"
   xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <rdf:Description rdf:about="#Reaction_63">
    <dcterms:created>
      <rdf:Description>
        <dcterms:W3CDTF>2014-06-20T13:08:17Z</dcterms:W3CDTF>
      </rdf:Description>
    </dcterms:created>
    <CopasiMT:is rdf:resource="urn:miriam:kegg.reaction:R02740"/>
  </rdf:Description>
</rdf:RDF>

        </MiriamAnnotation>
        <ListOfSubstrates>
          <Substrate metabolite="Metabolite_13" stoichiometry="1"/>
        </ListOfSubstrates>
        <ListOfProducts>
          <Product metabolite="Metabolite_17" stoichiometry="1"/>
        </ListOfProducts>
        <ListOfConstants>
          <Constant key="Parameter_4780" name="k1" value="931.69"/>
          <Constant key="Parameter_4779" name="k2" value="2273.32"/>
        </ListOfConstants>
        <KineticLaw function="Function_14" unitType="Default" scalingCompartment="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]">
          <ListOfCallParameters>
            <CallParameter functionParameter="FunctionParameter_62">
              <SourceParameter reference="Parameter_4780"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_39">
              <SourceParameter reference="Metabolite_13"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_67">
              <SourceParameter reference="Parameter_4779"/>
            </CallParameter>
            <CallParameter functionParameter="FunctionParameter_71">
              <SourceParameter reference="Metabolite_17"/>
            </CallParameter>
          </ListOfCallParameters>
        </KineticLaw>
      </Reaction>
    </ListOfReactions>
    <ListOfModelParameterSets activeSet="ModelParameterSet_0">
      <ModelParameterSet key="ModelParameterSet_0" name="Initial State">
        <ModelParameterGroup cn="String=Initial Time" type="Group">
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP" value="0" type="Model" simulationType="time"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Compartment Sizes" type="Group">
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary]" value="0.0055" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons]" value="0.45" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes]" value="0.25" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space]" value="0.2" type="Compartment" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon]" value="0.0237" type="Compartment" simulationType="ode"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery]" value="0.0055" type="Compartment" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Species Values" type="Group">
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[O2]" value="2.428325803378269e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[CO2]" value="7.314919938347404e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[GLC]" value="1.529044846313442e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[LAC]" value="1.137660849447695e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[dHb]" value="1.583305213193061e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLC]" value="3.090112737105681e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[G6P]" value="3.0524270007669e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ATP]" value="6.127934868601257e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[F6P]" value="1.250907966952481e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GAP]" value="2.919877019394538e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADH]" value="4.434518839407636e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PEP]" value="8.258172421834698e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PYR]" value="3.549858659939741e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[LAC]" value="3.80277364117232e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[O2]" value="8.092515130542068e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PCr]" value="3.90406530663399e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[Na+]" value="4.208908469671494e+021" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLU]" value="8.129891416500052e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ADP]" value="3.028051321764267e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[AMP]" value="1.626389250681618e+018" type="Species" simulationType="reactions">
            <InitialExpression>
              &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[ANP],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ADP],Reference=InitialConcentration>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ATP],Reference=InitialConcentration>
            </InitialExpression>
          </ModelParameter>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[Cr]" value="9.645753720866011e+020" type="Species" simulationType="reactions">
            <InitialExpression>
              &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PCr_total],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PCr],Reference=InitialConcentration>
            </InitialExpression>
          </ModelParameter>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NAD]" value="5.518468488159237e+019" type="Species" simulationType="reactions">
            <InitialExpression>
              &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_neurons],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADH],Reference=InitialConcentration>
            </InitialExpression>
          </ModelParameter>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[G6L]" value="813318075628842.5" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[P6G]" value="7.813535401252623e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[Ru5P]" value="1.827541174478298e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[X5P]" value="5.604726497612037e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[R5P]" value="7318574392544925" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[S7P]" value="3.130658216339239e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[E4P]" value="1.764761824055239e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADPH]" value="7.892119220450502e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADP]" value="600158224384.0026" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLC]" value="1.716705090944116e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP]" value="2.733304119026984e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[G6P]" value="1.025731514582645e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[F6P]" value="4.203553508299108e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GAP]" value="3.318437699139018e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADH]" value="8.722905019164804e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PEP]" value="1.392302435841128e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PYR]" value="2.358429163872089e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[LAC]" value="2.054771392175482e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[O2]" value="6.907587977715929e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PCr]" value="5.679362981563426e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[Na+]" value="2.413298924012218e+021" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLU]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ADP]" value="6.699021928558666e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[AMP]" value="1.784625177196497e+019" type="Species" simulationType="reactions">
            <InitialExpression>
              &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[ANP],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ADP],Reference=InitialConcentration>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP],Reference=InitialConcentration>
            </InitialExpression>
          </ModelParameter>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[Cr]" value="6.959740939343659e+020" type="Species" simulationType="reactions">
            <InitialExpression>
              &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PCr_total],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PCr],Reference=InitialConcentration>
            </InitialExpression>
          </ModelParameter>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NAD]" value="2.43988748258352e+019" type="Species" simulationType="reactions">
            <InitialExpression>
              &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_astrocytes],Reference=InitialValue>-&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADH],Reference=InitialConcentration>
            </InitialExpression>
          </ModelParameter>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[G6L]" value="451323396597124.9" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[P6G]" value="2.715418873587212e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[Ru5P]" value="1.015253689824934e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[X5P]" value="3.113556641479526e+018" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[R5P]" value="3915892215031140" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[S7P]" value="4.165675239478732e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[E4P]" value="8.580623363765201e+017" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADP]" value="415074990658.6348" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADPH]" value="4.384510668668477e+019" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLC]" value="1.373409754370402e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[LAC]" value="1.623291062898339e+020" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLU]" value="0" type="Species" simulationType="reactions"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[Na+]" value="1.806642537e+022" type="Species" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[O2]" value="2.762356439073005e+019" type="Species" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[CO2]" value="3.974613581400024e+018" type="Species" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[GLC]" value="1.589845432560009e+019" type="Species" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[LAC]" value="1.0367117091485e+018" type="Species" simulationType="fixed"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Initial Global Quantities" type="Group">
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_GLC]" value="0.105" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_G6P]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_F6P_PGI]" value="0.06" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_F6P_PFK]" value="0.18" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_I_ATP]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[nH]" value="4" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_neurons]" value="0.22" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NADH_total_astrocytes]" value="0.22" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NAD_neurons]" value="0.2036362432944397" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NAD_astrocytes]" value="0.1620611116553282" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[ANP]" value="2.379" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[q_AK]" value="0.92" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ATP]" value="0.01532" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ADP]" value="0.00107" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_O2]" value="0.0029658" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_PYR]" value="0.06320000000000001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PCr_total]" value="5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Cr_neurons]" value="3.559366254740929" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Cr_astrocytes]" value="4.62276790021821" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ATPase_neurons]" value="0.04889" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ATPase_astrocytes]" value="0.035657" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ec_LAC (wrt extracellular space)]" value="0.0325" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_0]" value="0.012" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[delta_F]" value="0.42" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_0]" value="200" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_end]" value="40" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_1]" value="2" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in]" value="0.012" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_out]" value="0.012" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[tau_v]" value="35" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Hb.OP]" value="8.6" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Sm_g]" value="10500" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Sm_n]" value="40500" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[k_pump]" value="3.17e-007" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m\,Na-pump]" value="0.4243" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_G6P_GLYS]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[delta_GLY]" value="62" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_GLY]" value="1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[g_Na_neurons]" value="0.0039" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[g_Na_astrocytes]" value="0.00325" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vm]" value="-70" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[RT]" value="2577340" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F]" value="96500" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[vn_1]" value="0.041" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[vn_2]" value="1.44" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[t_stim_tp]" value="2" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[is_stimulated]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[v_stim]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[R_Na_GLU]" value="0.075" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[V_eg_max_GLU]" value="0.0208" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_GLU]" value="0.05" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[V_gn_max_GLU]" value="0.3" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[delta_HK]" value="0.6" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[BOLD signal]" value="0" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[E0]" value="0.241844702566212" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ATP(ATPase)]" value="0.001" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NULL]" value="0" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ne_LAC (wrt extracellular space)]" value="0.44505" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PS_cap_astrocytes (wrt capillaries)]" value="11.16181818" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PS_cap_neuron (wrt capillaries)]" value="18.01636363" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_eg_GLU (wrt extracellular space)]" value="0.026" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_ce (Aubert)]" value="9" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ce_GLC (wrt capillaries) (Aubert)]" value="4.290909090909091" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_eg_GLC (wrt astrocytes) (Aubert) ]" value="1275" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_eg (Aubert)]" value="9" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_en (Aubert)]" value="9" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_en_GLC (wrt neurons) (Aubert)]" value="11767.5" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_cg (Aubert)]" value="9" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_cg_GLC (wrt capillaries) (Aubert)]" value="0.4227272727272727" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ec_LAC (wrt extracellular space) (Aubert)]" value="0.0058725" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_gc_LAC (wrt astrocytes) (Aubert)]" value="0.004350000000000001" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ge_LAC (wrt astrocytes) (Aubert)]" value="0.057" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ne_LAC (wrt neurons) (Aubert)]" value="0.2175" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_ne (Aubert)]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_ge (Aubert)]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_gc (Aubert)]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_ec (Aubert)]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PS_cap_astrocytes (wrt capillaries) (Aubert)]" value="4.704545454545455" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PS_cap_neuron (wrt capillaries) (Aubert)]" value="40.50000000000001" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_O2 (Aubert)]" value="0.0361" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[nh_O2 (Aubert)]" value="2.73" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_f_PGI (Cloutier)]" value="0.5" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_r_PGI (Cloutier)]" value="0.45" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ce_GLC (Aubert)]" value="0.118" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_cg_GLC  (Aubert)]" value="0.009299999999999999" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_eg_GLC (Aubert) ]" value="1020" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_en_GLC  (Aubert)]" value="5230" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf]" value="0.75" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[_PScap]" value="1.1" type="ModelValue" simulationType="fixed"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[f_CBF_dyn]" value="1" type="ModelValue" simulationType="assignment"/>
          <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[stimulus]" value="-3.870888684215236e+045" type="ModelValue" simulationType="assignment"/>
        </ModelParameterGroup>
        <ModelParameterGroup cn="String=Kinetic Parameters" type="Group">
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)],ParameterGroup=Parameters,Parameter=k_HK" value="0.01" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)],ParameterGroup=Parameters,Parameter=ATP" value="2.733304119026984e+020" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)],ParameterGroup=Parameters,Parameter=K_I_G6P" value="0.02" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=k_PFK" value="0.44" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=ATP" value="6.127934868601257e+020" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ATP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=K_I_ATP" value="1" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_I_ATP],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=nH" value="4" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[nH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=K_m_F6P" value="0.18" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=k_PFK" value="0.2" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=ATP" value="2.733304119026984e+020" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=K_I_ATP" value="1" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_I_ATP],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=nH" value="4" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[nH],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],ParameterGroup=Parameters,Parameter=K_m_F6P" value="0.18" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_neurons (R01061\, R01512\, R01518\, R00658)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_neurons (R01061\, R01512\, R01518\, R00658)],ParameterGroup=Parameters,Parameter=k_PGK" value="10" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_neurons (R01061\, R01512\, R01518\, R00658)],ParameterGroup=Parameters,Parameter=ADP" value="3.028051321764267e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ADP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_neurons (R01061\, R01512\, R01518\, R00658)],ParameterGroup=Parameters,Parameter=NAD" value="5.518468488159237e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NAD],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)],ParameterGroup=Parameters,Parameter=k_PGK" value="3" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)],ParameterGroup=Parameters,Parameter=ADP" value="6.699021928558666e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ADP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)],ParameterGroup=Parameters,Parameter=NAD" value="2.43988748258352e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NAD],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_neurons (R00200)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_neurons (R00200)],ParameterGroup=Parameters,Parameter=k_PK" value="44" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_neurons (R00200)],ParameterGroup=Parameters,Parameter=ADP" value="3.028051321764267e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ADP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_astrocytes (R00200)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_astrocytes (R00200)],ParameterGroup=Parameters,Parameter=k_PK" value="20" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_astrocytes (R00200)],ParameterGroup=Parameters,Parameter=ADP" value="6.699021928558666e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ADP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=v_max_mito" value="0.1" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=K_m_PYR" value="0.06320000000000001" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_PYR],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=ADP" value="3.028051321764267e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ADP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=K_m_ADP" value="0.00107" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ADP],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=K_m_O2" value="0.0029658" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_O2],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=ATP" value="6.127934868601257e+020" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ATP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=alpha" value="5" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],ParameterGroup=Parameters,Parameter=beta" value="20" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=v_max_mito" value="0.01" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=K_m_PYR" value="0.06320000000000001" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_PYR],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=ADP" value="6.699021928558666e+019" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ADP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=K_m_ADP" value="0.00107" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ADP],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=K_m_O2" value="0.0029658" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_O2],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=ATP" value="2.733304119026984e+020" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP],Reference=InitialParticleNumber>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=alpha" value="5" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=beta" value="20" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_neurons (Aubert)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_neurons (Aubert)],ParameterGroup=Parameters,Parameter=Vmax" value="11767.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_en_GLC (wrt neurons) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_neurons (Aubert)],ParameterGroup=Parameters,Parameter=K" value="9" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_en (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_neurons (Aubert)],ParameterGroup=Parameters,Parameter=sf" value="0.75" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_astrocytes (Aubert)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_astrocytes (Aubert)],ParameterGroup=Parameters,Parameter=Vmax" value="1275" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_eg_GLC (wrt astrocytes) (Aubert) ],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_astrocytes (Aubert)],ParameterGroup=Parameters,Parameter=K" value="9" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_eg (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_astrocytes (Aubert)],ParameterGroup=Parameters,Parameter=sf" value="0.75" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_ec (Aubert)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_ec (Aubert)],ParameterGroup=Parameters,Parameter=Vmax" value="4.290909090909091" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ce_GLC (wrt capillaries) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_ec (Aubert)],ParameterGroup=Parameters,Parameter=K" value="9" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_ce (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_ec (Aubert)],ParameterGroup=Parameters,Parameter=sf" value="0.75" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_astrocytes (Aubert)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_astrocytes (Aubert)],ParameterGroup=Parameters,Parameter=Vmax" value="0.4227272727272727" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_cg_GLC (wrt capillaries) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_astrocytes (Aubert)],ParameterGroup=Parameters,Parameter=K" value="9" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_GLC_cg (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_astrocytes (Aubert)],ParameterGroup=Parameters,Parameter=sf" value="0.75" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[sf],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_ec_capillary]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_ec_capillary],ParameterGroup=Parameters,Parameter=Vmax" value="0.0058725" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ec_LAC (wrt extracellular space) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_ec_capillary],ParameterGroup=Parameters,Parameter=K" value="0.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_ec (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_neurons_ec]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_neurons_ec],ParameterGroup=Parameters,Parameter=Vmax" value="0.2175" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ne_LAC (wrt neurons) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_neurons_ec],ParameterGroup=Parameters,Parameter=K" value="0.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_ne (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_ec]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_ec],ParameterGroup=Parameters,Parameter=Vmax" value="0.057" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_ge_LAC (wrt astrocytes) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_ec],ParameterGroup=Parameters,Parameter=K" value="0.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_ge (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_capillary]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_capillary],ParameterGroup=Parameters,Parameter=Vmax" value="0.004350000000000001" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_gc_LAC (wrt astrocytes) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_capillary],ParameterGroup=Parameters,Parameter=K" value="0.5" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_T_LAC_gc (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],ParameterGroup=Parameters,Parameter=PScap" value="40.50000000000001" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[PS_cap_neuron (wrt capillaries) (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],ParameterGroup=Parameters,Parameter=Volume" value="0.45" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],ParameterGroup=Parameters,Parameter=KO2" value="0.0361" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_O2 (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],ParameterGroup=Parameters,Parameter=HbOP" value="8.6" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Hb.OP],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],ParameterGroup=Parameters,Parameter=nh" value="2.73" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[nh_O2 (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],ParameterGroup=Parameters,Parameter=PScap" value="10" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],ParameterGroup=Parameters,Parameter=Volume" value="0.25" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],ParameterGroup=Parameters,Parameter=KO2" value="0.0361" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_O2 (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],ParameterGroup=Parameters,Parameter=HbOP" value="8.6" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Hb.OP],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],ParameterGroup=Parameters,Parameter=nh" value="2.73" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[nh_O2 (Aubert)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_artery_capillary]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_artery_capillary],ParameterGroup=Parameters,Parameter=F_in" value="0.012" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_artery_capillary],ParameterGroup=Parameters,Parameter=V_c" value="0.0055" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_artery_capillary]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_artery_capillary],ParameterGroup=Parameters,Parameter=F_in" value="0.012" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_artery_capillary],ParameterGroup=Parameters,Parameter=V_c" value="0.0055" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_capillary_artery]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_capillary_artery],ParameterGroup=Parameters,Parameter=F_in" value="0.012" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_capillary_artery],ParameterGroup=Parameters,Parameter=V_c" value="0.0055" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CO2_exchange_capillary_artery]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CO2_exchange_capillary_artery],ParameterGroup=Parameters,Parameter=F_in" value="0.012" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CO2_exchange_capillary_artery],ParameterGroup=Parameters,Parameter=V_c" value="0.0055" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=Sm" value="40500" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Sm_n],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=k_pump" value="3.17e-007" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[k_pump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=Km_pump" value="0.4243" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m\,Na-pump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=Volume" value="0.45" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=Sm" value="10500" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Sm_g],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=k_pump" value="3.17e-007" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[k_pump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=Km_pump" value="0.4243" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m\,Na-pump],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],ParameterGroup=Parameters,Parameter=Volume" value="0.25" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],ParameterGroup=Parameters,Parameter=Sm" value="40500" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Sm_n],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],ParameterGroup=Parameters,Parameter=gNA" value="0.0039" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[g_Na_neurons],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],ParameterGroup=Parameters,Parameter=F" value="96500" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],ParameterGroup=Parameters,Parameter=RT" value="2577340" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[RT],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],ParameterGroup=Parameters,Parameter=Vm" value="-70" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vm],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],ParameterGroup=Parameters,Parameter=Volume" value="0.45" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],ParameterGroup=Parameters,Parameter=Sm" value="10500" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Sm_g],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],ParameterGroup=Parameters,Parameter=gNA" value="0.0039" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],ParameterGroup=Parameters,Parameter=F" value="96500" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],ParameterGroup=Parameters,Parameter=RT" value="2577340" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[RT],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],ParameterGroup=Parameters,Parameter=Vm" value="-70" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vm],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],ParameterGroup=Parameters,Parameter=Volume" value="0.25" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Reference=InitialVolume>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_extracellular_space_neurons  (stimulation)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_extracellular_space_neurons  (stimulation)],ParameterGroup=Parameters,Parameter=vstim" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[v_stim],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_neurons_extracellular_space]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_neurons_extracellular_space],ParameterGroup=Parameters,Parameter=vSTIM" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[v_stim],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_neurons_extracellular_space],ParameterGroup=Parameters,Parameter=ratio_Na_GLU" value="0.075" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[R_Na_GLU],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_neurons_extracellular_space],ParameterGroup=Parameters,Parameter=Km_GLU" value="0.05" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_GLU],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_extracellular_space_astrocytes]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_extracellular_space_astrocytes],ParameterGroup=Parameters,Parameter=Vmax_GLU" value="0.026" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Vmax_eg_GLU (wrt extracellular space)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_extracellular_space_astrocytes],ParameterGroup=Parameters,Parameter=K_m_GLU" value="0.05" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_GLU],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons],ParameterGroup=Parameters,Parameter=Vmax_GLU" value="0.3" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[V_gn_max_GLU],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons],ParameterGroup=Parameters,Parameter=K_m_GLU" value="0.05" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_GLU],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons],ParameterGroup=Parameters,Parameter=K_m_ATP" value="0.01532" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ATP],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[inflow of dHb]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[inflow of dHb],ParameterGroup=Parameters,Parameter=F_in" value="0.012" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[outflow of dHb]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[outflow of dHb],ParameterGroup=Parameters,Parameter=F_out" value="0.012" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_out],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_neurons (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_neurons (n.a.)],ParameterGroup=Parameters,Parameter=VmaxATPase" value="0.07000000000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_neurons (n.a.)],ParameterGroup=Parameters,Parameter=Km_ATP" value="0.001" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ATP(ATPase)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_astrocytes (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=VmaxATPase" value="0.035" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=Km_ATP" value="0.001" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[K_m_ATP(ATPase)],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_neurons (R00127)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_neurons (R00127)],ParameterGroup=Parameters,Parameter=k1" value="1000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_neurons (R00127)],ParameterGroup=Parameters,Parameter=k2" value="920" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_astrocytes (R00127)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_astrocytes (R00127)],ParameterGroup=Parameters,Parameter=k1" value="1000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_astrocytes (R00127)],ParameterGroup=Parameters,Parameter=k2" value="920" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_astrocytes (R01881)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_astrocytes (R01881)],ParameterGroup=Parameters,Parameter=k1" value="0.5" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_astrocytes (R01881)],ParameterGroup=Parameters,Parameter=k2" value="0.01" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_neurons (R01881)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_neurons (R01881)],ParameterGroup=Parameters,Parameter=k1" value="0.5" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_neurons (R01881)],ParameterGroup=Parameters,Parameter=k2" value="0.01" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_astrocytes (R00703)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_astrocytes (R00703)],ParameterGroup=Parameters,Parameter=k1" value="780" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_astrocytes (R00703)],ParameterGroup=Parameters,Parameter=k2" value="32" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_neurons (R00703)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_neurons (R00703)],ParameterGroup=Parameters,Parameter=k1" value="2000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_neurons (R00703)],ParameterGroup=Parameters,Parameter=k2" value="15" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],ParameterGroup=Parameters,Parameter=Vmax" value="0.29057" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],ParameterGroup=Parameters,Parameter=K_S1" value="6.91392e-005" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],ParameterGroup=Parameters,Parameter=K_S2" value="1.31616e-005" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],ParameterGroup=Parameters,Parameter=Keq" value="22906.4" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],ParameterGroup=Parameters,Parameter=K_P1" value="0.0180932" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],ParameterGroup=Parameters,Parameter=K_P2" value="0.00050314" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],ParameterGroup=Parameters,Parameter=Vmax" value="0.586458" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],ParameterGroup=Parameters,Parameter=K_S1" value="6.91392e-005" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],ParameterGroup=Parameters,Parameter=K_S2" value="1.31616e-005" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],ParameterGroup=Parameters,Parameter=Keq" value="22906.4" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],ParameterGroup=Parameters,Parameter=K_P1" value="0.0180932" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],ParameterGroup=Parameters,Parameter=K_P2" value="0.00050314" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_neurons (R02035)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_neurons (R02035)],ParameterGroup=Parameters,Parameter=Vmax" value="0.372782" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_neurons (R02035)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0180932" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_neurons (R02035)],ParameterGroup=Parameters,Parameter=Keq" value="531174" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_neurons (R02035)],ParameterGroup=Parameters,Parameter=K_P1" value="2.28618" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_astrocytes (R02035)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_astrocytes (R02035)],ParameterGroup=Parameters,Parameter=Vmax" value="0.184701" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_astrocytes (R02035)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0180932" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_astrocytes (R02035)],ParameterGroup=Parameters,Parameter=Keq" value="531174" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_astrocytes (R02035)],ParameterGroup=Parameters,Parameter=K_P1" value="2.28618" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],ParameterGroup=Parameters,Parameter=Vmax" value="2.65764" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],ParameterGroup=Parameters,Parameter=K_S1" value="3.23421e-005" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],ParameterGroup=Parameters,Parameter=K_S2" value="3.11043e-006" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],ParameterGroup=Parameters,Parameter=Keq" value="40852000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],ParameterGroup=Parameters,Parameter=K_P1" value="0.0537179" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],ParameterGroup=Parameters,Parameter=K_P2" value="0.00050314" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],ParameterGroup=Parameters,Parameter=Vmax" value="1.31677" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],ParameterGroup=Parameters,Parameter=K_S1" value="3.23421e-005" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],ParameterGroup=Parameters,Parameter=K_S2" value="3.11043e-006" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],ParameterGroup=Parameters,Parameter=Keq" value="40852000" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],ParameterGroup=Parameters,Parameter=K_P1" value="0.00050314" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],ParameterGroup=Parameters,Parameter=K_P2" value="0.0537179" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons (R01529)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons (R01529)],ParameterGroup=Parameters,Parameter=Vmax" value="0.0156605" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons (R01529)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0537179" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons (R01529)],ParameterGroup=Parameters,Parameter=Keq" value="39.2574" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons (R01529)],ParameterGroup=Parameters,Parameter=K_P1" value="0.603002" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes (R01529)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes (R01529)],ParameterGroup=Parameters,Parameter=Vmax" value="0.00775925" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes (R01529)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0537179" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes (R01529)],ParameterGroup=Parameters,Parameter=Keq" value="39.2574" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes (R01529)],ParameterGroup=Parameters,Parameter=K_P1" value="0.603002" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes (R01056)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes (R01056)],ParameterGroup=Parameters,Parameter=Vmax" value="0.0008219840000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes (R01056)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0537179" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes (R01056)],ParameterGroup=Parameters,Parameter=Keq" value="35.4534" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes (R01056)],ParameterGroup=Parameters,Parameter=K_P1" value="0.778461" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons (R01056)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons (R01056)],ParameterGroup=Parameters,Parameter=Vmax" value="0.00165901" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons (R01056)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0537179" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons (R01056)],ParameterGroup=Parameters,Parameter=Keq" value="35.4534" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons (R01056)],ParameterGroup=Parameters,Parameter=K_P1" value="0.778461" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],ParameterGroup=Parameters,Parameter=Vmax" value="0.000244278" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],ParameterGroup=Parameters,Parameter=K_S1" value="0.000173625" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],ParameterGroup=Parameters,Parameter=K_S2" value="0.0005853869999999999" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],ParameterGroup=Parameters,Parameter=Keq" value="1652870" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],ParameterGroup=Parameters,Parameter=K_P1" value="0.168333" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],ParameterGroup=Parameters,Parameter=K_P2" value="0.192807" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],ParameterGroup=Parameters,Parameter=Vmax" value="0.0004930269999999999" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],ParameterGroup=Parameters,Parameter=K_S1" value="0.000173625" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],ParameterGroup=Parameters,Parameter=K_S2" value="0.0005853869999999999" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],ParameterGroup=Parameters,Parameter=Keq" value="1652870" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],ParameterGroup=Parameters,Parameter=K_P1" value="0.168333" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],ParameterGroup=Parameters,Parameter=K_P2" value="0.192807" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],ParameterGroup=Parameters,Parameter=Vmax" value="0.008039399999999999" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],ParameterGroup=Parameters,Parameter=K_S1" value="0.168333" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],ParameterGroup=Parameters,Parameter=K_S2" value="0.192807" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],ParameterGroup=Parameters,Parameter=Keq" value="0.323922" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],ParameterGroup=Parameters,Parameter=K_P1" value="0.0799745" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],ParameterGroup=Parameters,Parameter=K_P2" value="0.109681" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],ParameterGroup=Parameters,Parameter=Vmax" value="0.0162259" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],ParameterGroup=Parameters,Parameter=K_S1" value="0.168333" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],ParameterGroup=Parameters,Parameter=Keq" value="0.323922" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],ParameterGroup=Parameters,Parameter=K_P1" value="0.0799745" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],ParameterGroup=Parameters,Parameter=K_S2" value="0.192807" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],ParameterGroup=Parameters,Parameter=K_P2" value="0.109681" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],ParameterGroup=Parameters,Parameter=Vmax" value="0.000137124" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0799745" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],ParameterGroup=Parameters,Parameter=K_S2" value="0.168333" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],ParameterGroup=Parameters,Parameter=Keq" value="0.0777764" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],ParameterGroup=Parameters,Parameter=K_P1" value="0.603002" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],ParameterGroup=Parameters,Parameter=K_P2" value="0.109681" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],ParameterGroup=Parameters,Parameter=Vmax" value="0.000276758" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],ParameterGroup=Parameters,Parameter=K_S1" value="0.0799745" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],ParameterGroup=Parameters,Parameter=K_S2" value="0.168333" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],ParameterGroup=Parameters,Parameter=Keq" value="0.0777764" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],ParameterGroup=Parameters,Parameter=K_P1" value="0.603002" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],ParameterGroup=Parameters,Parameter=K_P2" value="0.109681" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase neurons (R07172)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase neurons (R07172)],ParameterGroup=Parameters,Parameter=k1" value="0.000423283" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase astrocytes (R07172)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase astrocytes (R07172)],ParameterGroup=Parameters,Parameter=k1" value="0.000209722" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_astrocytes (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_astrocytes (n.a.)],ParameterGroup=Parameters,Parameter=k1" value="0" type="ReactionParameter" simulationType="assignment">
              <InitialExpression>
                &lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NULL],Reference=InitialValue>
              </InitialExpression>
            </ModelParameter>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_neurons (n.a.)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_neurons (n.a.)],ParameterGroup=Parameters,Parameter=k1" value="0" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_astrocytes (R02740) (HS)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_astrocytes (R02740) (HS)],ParameterGroup=Parameters,Parameter=k1" value="931.6900000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_astrocytes (R02740) (HS)],ParameterGroup=Parameters,Parameter=k2" value="2273.32" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_neurons (R01786) (HeinrichSchuster)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_neurons (R01786) (HeinrichSchuster)],ParameterGroup=Parameters,Parameter=k_HK" value="0.022" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_neurons (R01786) (HeinrichSchuster)],ParameterGroup=Parameters,Parameter=K_I_G6P" value="0.02" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
          <ModelParameterGroup cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_neurons (R02740) (HS)]" type="Reaction">
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_neurons (R02740) (HS)],ParameterGroup=Parameters,Parameter=k1" value="931.6900000000001" type="ReactionParameter" simulationType="fixed"/>
            <ModelParameter cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_neurons (R02740) (HS)],ParameterGroup=Parameters,Parameter=k2" value="2273.32" type="ReactionParameter" simulationType="fixed"/>
          </ModelParameterGroup>
        </ModelParameterGroup>
      </ModelParameterSet>
    </ListOfModelParameterSets>
    <StateTemplate>
      <StateTemplateVariable objectReference="Model_4"/>
      <StateTemplateVariable objectReference="Compartment_9"/>
      <StateTemplateVariable objectReference="Metabolite_89"/>
      <StateTemplateVariable objectReference="Metabolite_37"/>
      <StateTemplateVariable objectReference="Metabolite_85"/>
      <StateTemplateVariable objectReference="Metabolite_33"/>
      <StateTemplateVariable objectReference="Metabolite_71"/>
      <StateTemplateVariable objectReference="Metabolite_19"/>
      <StateTemplateVariable objectReference="Metabolite_69"/>
      <StateTemplateVariable objectReference="Metabolite_17"/>
      <StateTemplateVariable objectReference="Metabolite_7"/>
      <StateTemplateVariable objectReference="Metabolite_49"/>
      <StateTemplateVariable objectReference="Metabolite_5"/>
      <StateTemplateVariable objectReference="Metabolite_1"/>
      <StateTemplateVariable objectReference="Metabolite_101"/>
      <StateTemplateVariable objectReference="Metabolite_67"/>
      <StateTemplateVariable objectReference="Metabolite_13"/>
      <StateTemplateVariable objectReference="Metabolite_79"/>
      <StateTemplateVariable objectReference="Metabolite_115"/>
      <StateTemplateVariable objectReference="Metabolite_105"/>
      <StateTemplateVariable objectReference="Metabolite_53"/>
      <StateTemplateVariable objectReference="Metabolite_111"/>
      <StateTemplateVariable objectReference="Metabolite_59"/>
      <StateTemplateVariable objectReference="Metabolite_25"/>
      <StateTemplateVariable objectReference="Metabolite_9"/>
      <StateTemplateVariable objectReference="Metabolite_117"/>
      <StateTemplateVariable objectReference="Metabolite_35"/>
      <StateTemplateVariable objectReference="Metabolite_75"/>
      <StateTemplateVariable objectReference="Metabolite_3"/>
      <StateTemplateVariable objectReference="Metabolite_63"/>
      <StateTemplateVariable objectReference="Metabolite_45"/>
      <StateTemplateVariable objectReference="Metabolite_99"/>
      <StateTemplateVariable objectReference="Metabolite_87"/>
      <StateTemplateVariable objectReference="Metabolite_51"/>
      <StateTemplateVariable objectReference="Metabolite_57"/>
      <StateTemplateVariable objectReference="Metabolite_109"/>
      <StateTemplateVariable objectReference="Metabolite_103"/>
      <StateTemplateVariable objectReference="Metabolite_23"/>
      <StateTemplateVariable objectReference="Metabolite_73"/>
      <StateTemplateVariable objectReference="Metabolite_31"/>
      <StateTemplateVariable objectReference="Metabolite_83"/>
      <StateTemplateVariable objectReference="Metabolite_11"/>
      <StateTemplateVariable objectReference="Metabolite_15"/>
      <StateTemplateVariable objectReference="Metabolite_65"/>
      <StateTemplateVariable objectReference="Metabolite_97"/>
      <StateTemplateVariable objectReference="Metabolite_47"/>
      <StateTemplateVariable objectReference="Metabolite_27"/>
      <StateTemplateVariable objectReference="Metabolite_81"/>
      <StateTemplateVariable objectReference="Metabolite_29"/>
      <StateTemplateVariable objectReference="Metabolite_107"/>
      <StateTemplateVariable objectReference="Metabolite_55"/>
      <StateTemplateVariable objectReference="Metabolite_91"/>
      <StateTemplateVariable objectReference="Metabolite_77"/>
      <StateTemplateVariable objectReference="Metabolite_113"/>
      <StateTemplateVariable objectReference="Metabolite_119"/>
      <StateTemplateVariable objectReference="Metabolite_43"/>
      <StateTemplateVariable objectReference="Metabolite_39"/>
      <StateTemplateVariable objectReference="Metabolite_95"/>
      <StateTemplateVariable objectReference="Metabolite_61"/>
      <StateTemplateVariable objectReference="Metabolite_93"/>
      <StateTemplateVariable objectReference="Metabolite_41"/>
      <StateTemplateVariable objectReference="Metabolite_21"/>
      <StateTemplateVariable objectReference="ModelValue_8"/>
      <StateTemplateVariable objectReference="ModelValue_9"/>
      <StateTemplateVariable objectReference="ModelValue_17"/>
      <StateTemplateVariable objectReference="ModelValue_18"/>
      <StateTemplateVariable objectReference="ModelValue_27"/>
      <StateTemplateVariable objectReference="ModelValue_28"/>
      <StateTemplateVariable objectReference="ModelValue_46"/>
      <StateTemplateVariable objectReference="ModelValue_47"/>
      <StateTemplateVariable objectReference="ModelValue_53"/>
      <StateTemplateVariable objectReference="ModelValue_54"/>
      <StateTemplateVariable objectReference="ModelValue_62"/>
      <StateTemplateVariable objectReference="ModelValue_63"/>
      <StateTemplateVariable objectReference="ModelValue_66"/>
      <StateTemplateVariable objectReference="ModelValue_68"/>
      <StateTemplateVariable objectReference="ModelValue_69"/>
      <StateTemplateVariable objectReference="ModelValue_70"/>
      <StateTemplateVariable objectReference="ModelValue_71"/>
      <StateTemplateVariable objectReference="ModelValue_72"/>
      <StateTemplateVariable objectReference="ModelValue_77"/>
      <StateTemplateVariable objectReference="ModelValue_78"/>
      <StateTemplateVariable objectReference="ModelValue_89"/>
      <StateTemplateVariable objectReference="ModelValue_90"/>
      <StateTemplateVariable objectReference="Metabolite_121"/>
      <StateTemplateVariable objectReference="Metabolite_123"/>
      <StateTemplateVariable objectReference="Metabolite_125"/>
      <StateTemplateVariable objectReference="Metabolite_127"/>
      <StateTemplateVariable objectReference="Metabolite_129"/>
      <StateTemplateVariable objectReference="Compartment_1"/>
      <StateTemplateVariable objectReference="Compartment_3"/>
      <StateTemplateVariable objectReference="Compartment_5"/>
      <StateTemplateVariable objectReference="Compartment_7"/>
      <StateTemplateVariable objectReference="Compartment_11"/>
      <StateTemplateVariable objectReference="ModelValue_0"/>
      <StateTemplateVariable objectReference="ModelValue_1"/>
      <StateTemplateVariable objectReference="ModelValue_2"/>
      <StateTemplateVariable objectReference="ModelValue_3"/>
      <StateTemplateVariable objectReference="ModelValue_4"/>
      <StateTemplateVariable objectReference="ModelValue_5"/>
      <StateTemplateVariable objectReference="ModelValue_6"/>
      <StateTemplateVariable objectReference="ModelValue_7"/>
      <StateTemplateVariable objectReference="ModelValue_10"/>
      <StateTemplateVariable objectReference="ModelValue_11"/>
      <StateTemplateVariable objectReference="ModelValue_12"/>
      <StateTemplateVariable objectReference="ModelValue_13"/>
      <StateTemplateVariable objectReference="ModelValue_14"/>
      <StateTemplateVariable objectReference="ModelValue_15"/>
      <StateTemplateVariable objectReference="ModelValue_16"/>
      <StateTemplateVariable objectReference="ModelValue_19"/>
      <StateTemplateVariable objectReference="ModelValue_20"/>
      <StateTemplateVariable objectReference="ModelValue_21"/>
      <StateTemplateVariable objectReference="ModelValue_22"/>
      <StateTemplateVariable objectReference="ModelValue_23"/>
      <StateTemplateVariable objectReference="ModelValue_24"/>
      <StateTemplateVariable objectReference="ModelValue_25"/>
      <StateTemplateVariable objectReference="ModelValue_26"/>
      <StateTemplateVariable objectReference="ModelValue_29"/>
      <StateTemplateVariable objectReference="ModelValue_30"/>
      <StateTemplateVariable objectReference="ModelValue_31"/>
      <StateTemplateVariable objectReference="ModelValue_32"/>
      <StateTemplateVariable objectReference="ModelValue_33"/>
      <StateTemplateVariable objectReference="ModelValue_34"/>
      <StateTemplateVariable objectReference="ModelValue_35"/>
      <StateTemplateVariable objectReference="ModelValue_36"/>
      <StateTemplateVariable objectReference="ModelValue_37"/>
      <StateTemplateVariable objectReference="ModelValue_38"/>
      <StateTemplateVariable objectReference="ModelValue_39"/>
      <StateTemplateVariable objectReference="ModelValue_40"/>
      <StateTemplateVariable objectReference="ModelValue_41"/>
      <StateTemplateVariable objectReference="ModelValue_42"/>
      <StateTemplateVariable objectReference="ModelValue_43"/>
      <StateTemplateVariable objectReference="ModelValue_44"/>
      <StateTemplateVariable objectReference="ModelValue_45"/>
      <StateTemplateVariable objectReference="ModelValue_48"/>
      <StateTemplateVariable objectReference="ModelValue_49"/>
      <StateTemplateVariable objectReference="ModelValue_50"/>
      <StateTemplateVariable objectReference="ModelValue_51"/>
      <StateTemplateVariable objectReference="ModelValue_52"/>
      <StateTemplateVariable objectReference="ModelValue_55"/>
      <StateTemplateVariable objectReference="ModelValue_56"/>
      <StateTemplateVariable objectReference="ModelValue_57"/>
      <StateTemplateVariable objectReference="ModelValue_58"/>
      <StateTemplateVariable objectReference="ModelValue_59"/>
      <StateTemplateVariable objectReference="ModelValue_60"/>
      <StateTemplateVariable objectReference="ModelValue_61"/>
      <StateTemplateVariable objectReference="ModelValue_64"/>
      <StateTemplateVariable objectReference="ModelValue_65"/>
      <StateTemplateVariable objectReference="ModelValue_67"/>
      <StateTemplateVariable objectReference="ModelValue_73"/>
      <StateTemplateVariable objectReference="ModelValue_74"/>
      <StateTemplateVariable objectReference="ModelValue_75"/>
      <StateTemplateVariable objectReference="ModelValue_76"/>
      <StateTemplateVariable objectReference="ModelValue_79"/>
      <StateTemplateVariable objectReference="ModelValue_80"/>
      <StateTemplateVariable objectReference="ModelValue_81"/>
      <StateTemplateVariable objectReference="ModelValue_82"/>
      <StateTemplateVariable objectReference="ModelValue_83"/>
      <StateTemplateVariable objectReference="ModelValue_84"/>
      <StateTemplateVariable objectReference="ModelValue_85"/>
      <StateTemplateVariable objectReference="ModelValue_86"/>
      <StateTemplateVariable objectReference="ModelValue_87"/>
      <StateTemplateVariable objectReference="ModelValue_88"/>
    </StateTemplate>
    <InitialState type="initialState">
      0 0.0237 6.699021928558666e+019 3.028051321764267e+019 2.413298924012218e+021 4.208908469671494e+021 3.318437699139018e+017 2.919877019394538e+017 4.203553508299108e+018 1.250907966952481e+019 1.137660849447695e+018 1.827541174478298e+017 1.529044846313442e+019 2.428325803378269e+019 1.015253689824934e+017 1.025731514582645e+019 3.0524270007669e+019 2.054771392175482e+020 1.373409754370402e+020 3915892215031140 7318574392544925 415074990658.6348 7.892119220450502e+019 3.549858659939741e+019 1.583305213193061e+017 1.623291062898339e+020 8.129891416500052e+020 1.392302435841128e+017 7.314919938347404e+018 1.716705090944116e+020 813318075628842.5 2.715418873587212e+017 0 5.604726497612037e+018 1.764761824055239e+018 8.580623363765201e+017 3.113556641479526e+018 8.258172421834698e+017 8.722905019164804e+018 3.90406530663399e+020 5.679362981563426e+019 3.090112737105681e+020 6.127934868601257e+020 2.733304119026984e+020 451323396597124.9 7.813535401252623e+017 3.80277364117232e+020 6.907587977715929e+018 8.092515130542068e+018 4.165675239478732e+019 3.130658216339239e+020 1.784625177196497e+019 2.358429163872089e+019 4.384510668668477e+019 0 5.518468488159237e+019 1.626389250681618e+018 2.43988748258352e+019 600158224384.0026 6.959740939343659e+020 9.645753720866011e+020 4.434518839407636e+018 0.2036362432944397 0.1620611116553282 3.559366254740929 4.62276790021821 0.012 0.012 0 0 0 0.241844702566212 4.290909090909091 1275 11767.5 0.4227272727272727 0.0058725 0.004350000000000001 0.057 0.2175 4.704545454545455 40.50000000000001 1 -3.870888684215236e+045 1.806642537e+022 2.762356439073005e+019 3.974613581400024e+018 1.589845432560009e+019 1.0367117091485e+018 0.0055 0.45 0.25 0.2 0.0055 0.105 0.5 0.06 0.18 1 4 0.22 0.22 2.379 0.92 0.01532 0.00107 0.0029658 0.06320000000000001 5 0.04889 0.035657 0.0325 0.012 0.42 200 40 2 35 8.6 10500 40500 3.17e-007 0.4243 0.5 62 1 0.0039 0.00325 -70 2577340 96500 0.041 1.44 2 0.075 0.0208 0.05 0.3 0.6 0.001 0 0.44505 11.16181818 18.01636363 0.026 9 9 9 9 0.5 0.5 0.5 0.5 0.0361 2.73 0.5 0.45 0.118 0.009299999999999999 1020 5230 0.75 1.1 
    </InitialState>
  </Model>
  <ListOfTasks>
    <Task key="Task_12" name="Steady-State" type="steadyState" scheduled="false" updateModel="false">
      <Report reference="Report_8" target="" append="0" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="JacobianRequested" type="bool" value="1"/>
        <Parameter name="StabilityAnalysisRequested" type="bool" value="1"/>
      </Problem>
      <Method name="Enhanced Newton" type="EnhancedNewton">
        <Parameter name="Resolution" type="unsignedFloat" value="1e-009"/>
        <Parameter name="Derivation Factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Use Newton" type="bool" value="1"/>
        <Parameter name="Use Integration" type="bool" value="1"/>
        <Parameter name="Use Back Integration" type="bool" value="1"/>
        <Parameter name="Accept Negative Concentrations" type="bool" value="0"/>
        <Parameter name="Iteration Limit" type="unsignedInteger" value="50"/>
        <Parameter name="Maximum duration for forward integration" type="unsignedFloat" value="1000000000"/>
        <Parameter name="Maximum duration for backward integration" type="unsignedFloat" value="1000000"/>
      </Method>
    </Task>
    <Task key="Task_11" name="Time-Course" type="timeCourse" scheduled="false" updateModel="false">
      <Report reference="Report_25" target="" append="1" confirmOverwrite="0"/>
      <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="1"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="100000"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1000"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="0"/>
        <Parameter name="OutputStartTime" type="float" value="100"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="Continue on Simultaneous Events" type="bool" value="0"/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="1e-012"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="1"/>
      </Method>
    </Task>
    <Task key="Task_10" name="Scan" type="scan" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="Subtask" type="unsignedInteger" value="0"/>
        <ParameterGroup name="ScanItems">
        </ParameterGroup>
        <Parameter name="Output in subtask" type="bool" value="0"/>
        <Parameter name="Adjust initial conditions" type="bool" value="0"/>
      </Problem>
      <Method name="Scan Framework" type="ScanFramework">
      </Method>
    </Task>
    <Task key="Task_9" name="Elementary Flux Modes" type="fluxMode" scheduled="false" updateModel="false">
      <Report reference="Report_7" target="" append="1" confirmOverwrite="1"/>
      <Problem>
      </Problem>
      <Method name="EFM Algorithm" type="EFMAlgorithm">
      </Method>
    </Task>
    <Task key="Task_8" name="Optimization" type="optimization" scheduled="false" updateModel="false">
      <Report reference="Report_6" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Subtask" type="cn" value="CN=Root,Vector=TaskList[Steady-State]"/>
        <ParameterText name="ObjectiveExpression" type="expression">
          ((&lt;CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[LAC],Reference=Concentration>-1.0)/1.0)^2
        </ParameterText>
        <Parameter name="Maximize" type="bool" value="0"/>
        <Parameter name="Randomize Start Values" type="bool" value="0"/>
        <Parameter name="Calculate Statistics" type="bool" value="1"/>
        <ParameterGroup name="OptimizationItemList">
          <ParameterGroup name="OptimizationItem">
            <Parameter name="LowerBound" type="cn" value="0.1"/>
            <Parameter name="ObjectCN" type="cn" value="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[_sf_Lac],Reference=InitialValue"/>
            <Parameter name="StartValue" type="float" value="0.564385"/>
            <Parameter name="UpperBound" type="cn" value="10"/>
          </ParameterGroup>
        </ParameterGroup>
        <ParameterGroup name="OptimizationConstraintList">
        </ParameterGroup>
      </Problem>
      <Method name="Particle Swarm" type="ParticleSwarm">
        <Parameter name="Iteration Limit" type="unsignedInteger" value="2000"/>
        <Parameter name="Swarm Size" type="unsignedInteger" value="50"/>
        <Parameter name="Std. Deviation" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_7" name="Parameter Estimation" type="parameterFitting" scheduled="false" updateModel="false">
      <Report reference="Report_5" target="" append="1" confirmOverwrite="1"/>
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
          <Parameter name="Threshold" type="unsignedInteger" value="5"/>
          <Parameter name="Weight" type="unsignedFloat" value="1"/>
        </ParameterGroup>
      </Problem>
      <Method name="Evolutionary Programming" type="EvolutionaryProgram">
        <Parameter name="Number of Generations" type="unsignedInteger" value="200"/>
        <Parameter name="Population Size" type="unsignedInteger" value="20"/>
        <Parameter name="Random Number Generator" type="unsignedInteger" value="1"/>
        <Parameter name="Seed" type="unsignedInteger" value="0"/>
      </Method>
    </Task>
    <Task key="Task_6" name="Metabolic Control Analysis" type="metabolicControlAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_4" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_12"/>
      </Problem>
      <Method name="MCA Method (Reder)" type="MCAMethod(Reder)">
        <Parameter name="Modulation Factor" type="unsignedFloat" value="1e-009"/>
        <Parameter name="Use Reder" type="bool" value="1"/>
        <Parameter name="Use Smallbone" type="bool" value="1"/>
      </Method>
    </Task>
    <Task key="Task_5" name="Lyapunov Exponents" type="lyapunovExponents" scheduled="false" updateModel="false">
      <Report reference="Report_3" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="ExponentNumber" type="unsignedInteger" value="3"/>
        <Parameter name="DivergenceRequested" type="bool" value="1"/>
        <Parameter name="TransientTime" type="float" value="0"/>
      </Problem>
      <Method name="Wolf Method" type="WolfMethod">
        <Parameter name="Orthonormalization Interval" type="unsignedFloat" value="1"/>
        <Parameter name="Overall time" type="unsignedFloat" value="1000"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="1e-012"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
      </Method>
    </Task>
    <Task key="Task_4" name="Time Scale Separation Analysis" type="timeScaleSeparationAnalysis" scheduled="false" updateModel="false">
      <Report reference="Report_2" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
      </Problem>
      <Method name="ILDM (LSODA,Deuflhard)" type="TimeScaleSeparation(ILDM,Deuflhard)">
        <Parameter name="Deuflhard Tolerance" type="unsignedFloat" value="1e-006"/>
      </Method>
    </Task>
    <Task key="Task_3" name="Sensitivities" type="sensitivities" scheduled="false" updateModel="false">
      <Report reference="Report_1" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="SubtaskType" type="unsignedInteger" value="1"/>
        <ParameterGroup name="TargetFunctions">
          <Parameter name="SingleObject" type="cn" value=""/>
          <Parameter name="ObjectListType" type="unsignedInteger" value="7"/>
        </ParameterGroup>
        <ParameterGroup name="ListOfVariables">
          <ParameterGroup name="Variables">
            <Parameter name="SingleObject" type="cn" value=""/>
            <Parameter name="ObjectListType" type="unsignedInteger" value="0"/>
          </ParameterGroup>
          <ParameterGroup name="Variables">
            <Parameter name="SingleObject" type="cn" value=""/>
            <Parameter name="ObjectListType" type="unsignedInteger" value="0"/>
          </ParameterGroup>
        </ParameterGroup>
      </Problem>
      <Method name="Sensitivities Method" type="SensitivitiesMethod">
        <Parameter name="Delta factor" type="unsignedFloat" value="0.001"/>
        <Parameter name="Delta minimum" type="unsignedFloat" value="1e-012"/>
      </Method>
    </Task>
    <Task key="Task_2" name="Moieties" type="moieties" scheduled="false" updateModel="false">
      <Problem>
      </Problem>
      <Method name="Householder Reduction" type="Householder">
      </Method>
    </Task>
    <Task key="Task_1" name="Cross Section" type="crosssection" scheduled="false" updateModel="false">
      <Problem>
        <Parameter name="AutomaticStepSize" type="bool" value="0"/>
        <Parameter name="StepNumber" type="unsignedInteger" value="100"/>
        <Parameter name="StepSize" type="float" value="0.01"/>
        <Parameter name="Duration" type="float" value="1"/>
        <Parameter name="TimeSeriesRequested" type="bool" value="1"/>
        <Parameter name="OutputStartTime" type="float" value="0"/>
        <Parameter name="Output Event" type="bool" value="0"/>
        <Parameter name="Start in Steady State" type="bool" value="0"/>
        <Parameter name="LimitCrossings" type="bool" value="0"/>
        <Parameter name="NumCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitOutTime" type="bool" value="0"/>
        <Parameter name="LimitOutCrossings" type="bool" value="0"/>
        <Parameter name="PositiveDirection" type="bool" value="1"/>
        <Parameter name="NumOutCrossingsLimit" type="unsignedInteger" value="0"/>
        <Parameter name="LimitUntilConvergence" type="bool" value="0"/>
        <Parameter name="ConvergenceTolerance" type="float" value="0"/>
        <Parameter name="Threshold" type="float" value="0"/>
        <Parameter name="DelayOutputUntilConvergence" type="bool" value="0"/>
        <Parameter name="OutputConvergenceTolerance" type="float" value="0"/>
        <ParameterText name="TriggerExpression" type="expression">
          
        </ParameterText>
        <Parameter name="SingleVariable" type="cn" value=""/>
        <Parameter name="Continue on Simultaneous Events" type="bool" value="0"/>
      </Problem>
      <Method name="Deterministic (LSODA)" type="Deterministic(LSODA)">
        <Parameter name="Integrate Reduced Model" type="bool" value="0"/>
        <Parameter name="Relative Tolerance" type="unsignedFloat" value="1e-006"/>
        <Parameter name="Absolute Tolerance" type="unsignedFloat" value="1e-012"/>
        <Parameter name="Max Internal Steps" type="unsignedInteger" value="10000"/>
        <Parameter name="Max Internal Step Size" type="unsignedFloat" value="0"/>
      </Method>
    </Task>
    <Task key="Task_13" name="Linear Noise Approximation" type="linearNoiseApproximation" scheduled="false" updateModel="false">
      <Report reference="Report_0" target="" append="1" confirmOverwrite="1"/>
      <Problem>
        <Parameter name="Steady-State" type="key" value="Task_12"/>
      </Problem>
      <Method name="Linear Noise Approximation" type="LinearNoiseApproximation">
      </Method>
    </Task>
  </ListOfTasks>
  <ListOfReports>
    <Report key="Report_8" name="Steady-State" taskType="steadyState" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Steady-State]"/>
      </Footer>
    </Report>
    <Report key="Report_7" name="Elementary Flux Modes" taskType="fluxMode" separator="&#x09;" precision="6">
      <Comment>
        Automatically generated report.
      </Comment>
      <Footer>
        <Object cn="CN=Root,Vector=TaskList[Elementary Flux Modes],Object=Result"/>
      </Footer>
    </Report>
    <Report key="Report_6" name="Optimization" taskType="optimization" separator="&#x09;" precision="6">
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
    <Report key="Report_5" name="Parameter Estimation" taskType="parameterFitting" separator="&#x09;" precision="6">
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
    <Report key="Report_3" name="Lyapunov Exponents" taskType="lyapunovExponents" separator="&#x09;" precision="6">
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
    <Report key="Report_2" name="Time Scale Separation Analysis" taskType="timeScaleSeparationAnalysis" separator="&#x09;" precision="6">
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
    <Report key="Report_1" name="Sensitivities" taskType="sensitivities" separator="&#x09;" precision="6">
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
    <Report key="Report_0" name="Linear Noise Approximation" taskType="linearNoiseApproximation" separator="&#x09;" precision="6">
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
    <Report key="Report_18" name="Fluxes at the start of the simulation" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[vCK_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLY_degradation_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLY_production_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[vLDH_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[vPGI_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_astrocytes (R00200)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],Reference=Flux"/>
      </Table>
    </Report>
    <Report key="Report_19" name="Fluxes_neurons" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[vCK_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_neurons (R01786)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[vLDH_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[vPGI_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_neurons (R01061\, R01512\, R01518\, R00658)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_neurons (R00200)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)],Reference=Flux"/>
      </Table>
    </Report>
    <Report key="Report_20" name="All_Species" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ATP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[CO2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[CO2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Cr_astrocytes],Reference=Value"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[Cr_neurons],Reference=Value"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[F6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[F6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[G6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[G6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GAP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GAP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLU],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLU],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLU],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NAD_astrocytes],Reference=Value"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[NAD_neurons],Reference=Value"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADH],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADH],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PCr],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PCr],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PEP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PEP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PYR],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PYR],Reference=Concentration"/>
      </Table>
    </Report>
    <Report key="Report_21" name="O2 Metabolism" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_artery_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[F_in],Reference=Value"/>
      </Table>
    </Report>
    <Report key="Report_22" name="ATP astrocyte influences" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[vATPase_astrocytes],Reference=Value"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[vCK_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_astrocytes (R00200)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],Reference=Flux"/>
      </Table>
    </Report>
    <Report key="Report_23" name="Flux at steady state" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="0">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_astrocytes (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_astrocytes_reverse (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_neurons (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_neurons_reverse (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_artery_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_capillary_artery],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_artery_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[E4P sink_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[E4P sink_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CO2_exchange_capillary_artery],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_ec],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_ec_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_neurons_ec],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase astrocytes (R07172)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase neurons (R07172)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes (R01056)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons (R01056)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes (R01529)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons (R01529)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_astrocytes (R02035)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_neurons (R02035)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons_reverse],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_astrocytes (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_ec (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_astrocytes (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_neurons (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[inflow of dHb],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[outflow of dHb],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_astrocytes (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_astrocytes_reverse (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_neurons (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_neurons_reverse (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_extracellular_space_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_neurons_extracellular_space],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLY_degradation_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLY_production_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_neurons (R01786)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_astrocytes (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_astrocytes_reverse (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_neurons (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_neurons_reverse (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_astrocytes (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_astrocytes_reverse (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_neurons (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_neurons_reverse (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_neurons (R01061\, R01512\, R01518\, R00658)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_astrocytes (R00200)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_neurons (R00200)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_extracellular_space_neurons  (stimulation)],Reference=Flux"/>
      </Table>
    </Report>
    <Report key="Report_24" name="Comparison" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLC],Reference=Concentration"/>
      </Table>
    </Report>
    <Report key="Report_25" name="Concentrations after Time Course" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ADP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ADP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[AMP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[AMP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[ATP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[ATP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[CO2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[CO2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[Cr],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[Cr],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[E4P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[E4P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[F6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[F6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[G6L],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[G6L],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[G6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[G6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GAP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GAP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLU],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLU],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLU],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLY],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADH],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADH],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADPH],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADPH],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NADP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NADP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[NAD],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[NAD],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[Na+],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[Na+],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[Na+],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[artery],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[O2],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[P6G],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[P6G],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PCr],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PCr],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PEP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PEP],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[PYR],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[PYR],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[R5P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[R5P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[Ru5P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[Ru5P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[S7P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[S7P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[X5P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[X5P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[dHb],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_astrocytes (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_astrocytes_reverse (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_neurons (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[AK_neurons_reverse (R00127)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ATPase_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_astrocytes (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_astrocytes_reverse (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_neurons (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CK_neurons_reverse (R01881)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[CO2_exchange_capillary_artery],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_artery_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_astrocytes (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_capillary_ec (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_astrocytes (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLC_exchange_extracellular_space_neurons (Aubert)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_astrocytes_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_extracellular_space_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLU_exchange_neurons_extracellular_space],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLY_degradation_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GLY_production_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_astrocytes (R01528)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[GND_neurons  (R01528)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_astrocytes (R01786)  (HeinrichSchuster)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[HK_neurons (R01786)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_astrocytes_ec],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_capillary_artery],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_ec_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LAC_exchange_neurons_ec],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_astrocytes (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_astrocytes_reverse (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_neurons (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LDH_neurons_reverse (R00703)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_astrocytes (n.a)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[LEAK_Na_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase astrocytes (R07172)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[NADPH oxidase neurons (R07172)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_astrocytes_extracellular_space (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_extracellular_space_neurons  (stimulation)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_neurons_extracellular_space (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_artery_capillary],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_astrocytes],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[O2_exchange_capillary_neurons],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_astrocytes (R04779\, R01070\, R01015)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PFK_neurons (R04779\, R01070\, R01015)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_astrocytes (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_astrocytes_reverse (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_neurons (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGI_neurons_reverse (R02740)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_astrocytes (R01061\, R01512\, R01518\, R00658)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PGK_neurons (R01061\, R01512\, R01518\, R00658)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_astrocytes (R00200)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[PK_neurons (R00200)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[R5P sink_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_astrocytes (R01056)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RKI_neurons (R01056)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_astrocytes (R01529)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[RPE_neurons (R01529)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_astrocytes (R02035)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[SOL_neurons (R02035)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_astrocytes (R01827)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TAL_neurons (R01827)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_astrocytes (R01641)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-1_neurons (R01641)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_astrocytes (R01830)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[TKL-2_neurons (R01830)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_astrocytes (R02736)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[ZWF_neurons (R02736)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[inflow of dHb],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_astrocytes (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[mitochondrial_respiration_neurons (n.a.)],Reference=Flux"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[outflow of dHb],Reference=Flux"/>
      </Table>
    </Report>
    <Report key="Report_26" name="BOLD shape / model time" taskType="timeCourse" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[BOLD signal],Reference=Value"/>
      </Table>
    </Report>
    <Report key="Report_27" name="Species at steady state (for revision)" taskType="steadyState" separator="&#x09;" precision="6">
      <Comment>
      </Comment>
      <Table printTitle="1">
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[extracellular_space],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[LAC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[G6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[G6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[F6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[astrocytes],Vector=Metabolites[F6P],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[capillary],Vector=Metabolites[GLC],Reference=SBMLId"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLC],Reference=Concentration"/>
        <Object cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[neurons],Vector=Metabolites[GLC],Reference=SBMLId"/>
      </Table>
    </Report>
  </ListOfReports>
  <ListOfPlots>
    <PlotSpecification name="Figure 3. Pane (c)" type="Plot2D" active="1">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
        <PlotItem name="Values[BOLD signal]|Time" type="Curve2D">
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Values[BOLD signal],Reference=Value"/>
          </ListOfChannels>
        </PlotItem>
      </ListOfPlotItems>
    </PlotSpecification>
    <PlotSpecification name="Figure 3. Pane (a)" type="Plot2D" active="1">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
        <PlotItem name="(Na+_exchange_extracellular_space_neurons  (stimulation)).Flux|Time" type="Curve2D">
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Reactions[Na+_exchange_extracellular_space_neurons  (stimulation)],Reference=Flux"/>
          </ListOfChannels>
        </PlotItem>
      </ListOfPlotItems>
    </PlotSpecification>
    <PlotSpecification name="Figure 3. Pane (b)" type="Plot2D" active="1">
      <Parameter name="log X" type="bool" value="0"/>
      <Parameter name="log Y" type="bool" value="0"/>
      <ListOfPlotItems>
        <PlotItem name="Compartments[venous balloon].Volume|Time" type="Curve2D">
          <Parameter name="Color" type="string" value="auto"/>
          <Parameter name="Line subtype" type="unsignedInteger" value="0"/>
          <Parameter name="Line type" type="unsignedInteger" value="0"/>
          <Parameter name="Line width" type="unsignedFloat" value="1"/>
          <Parameter name="Recording Activity" type="string" value="during"/>
          <Parameter name="Symbol subtype" type="unsignedInteger" value="0"/>
          <ListOfChannels>
            <ChannelSpec cn="CN=Root,Model=Brain Energy Metabolism with PPP,Reference=Time"/>
            <ChannelSpec cn="CN=Root,Model=Brain Energy Metabolism with PPP,Vector=Compartments[venous balloon],Reference=Volume"/>
          </ListOfChannels>
        </PlotItem>
      </ListOfPlotItems>
    </PlotSpecification>
  </ListOfPlots>
  <GUI>
  </GUI>
  <SBMLReference file="MODEL1603240000.xml">
    <SBMLMap SBMLid="ADP_astrocytes" COPASIkey="Metabolite_89"/>
    <SBMLMap SBMLid="ADP_neurons" COPASIkey="Metabolite_37"/>
    <SBMLMap SBMLid="AK_astrocytes" COPASIkey="Reaction_36"/>
    <SBMLMap SBMLid="AK_neurons" COPASIkey="Reaction_35"/>
    <SBMLMap SBMLid="AMP_astrocytes" COPASIkey="Metabolite_91"/>
    <SBMLMap SBMLid="AMP_neurons" COPASIkey="Metabolite_39"/>
    <SBMLMap SBMLid="ATPase_astrocytes" COPASIkey="Reaction_34"/>
    <SBMLMap SBMLid="ATPase_neurons" COPASIkey="Reaction_33"/>
    <SBMLMap SBMLid="BOLD_signal" COPASIkey="ModelValue_53"/>
    <SBMLMap SBMLid="Blood_flow_contribution_inkl__volume" COPASIkey="Function_53"/>
    <SBMLMap SBMLid="Blood_flow_contribution_to_capillary_GLC" COPASIkey="Reaction_20"/>
    <SBMLMap SBMLid="Blood_flow_contribution_to_capillary_LAC" COPASIkey="Reaction_21"/>
    <SBMLMap SBMLid="Blood_flow_contribution_to_capillary_O2" COPASIkey="Reaction_19"/>
    <SBMLMap SBMLid="CK_astrocytes_forward__R01881" COPASIkey="Reaction_37"/>
    <SBMLMap SBMLid="CK_neurons_forward__R01881" COPASIkey="Reaction_38"/>
    <SBMLMap SBMLid="CO2_artery" COPASIkey="Metabolite_125"/>
    <SBMLMap SBMLid="Cr_astrocytes" COPASIkey="Metabolite_93"/>
    <SBMLMap SBMLid="Cr_neurons" COPASIkey="Metabolite_41"/>
    <SBMLMap SBMLid="E0" COPASIkey="ModelValue_54"/>
    <SBMLMap SBMLid="E4P_astrocytes" COPASIkey="Metabolite_109"/>
    <SBMLMap SBMLid="E4P_neurons" COPASIkey="Metabolite_57"/>
    <SBMLMap SBMLid="F" COPASIkey="ModelValue_42"/>
    <SBMLMap SBMLid="F_0" COPASIkey="ModelValue_22"/>
    <SBMLMap SBMLid="F_in" COPASIkey="ModelValue_27"/>
    <SBMLMap SBMLid="F_out" COPASIkey="ModelValue_28"/>
    <SBMLMap SBMLid="Flow_of_CO2_between_capillary_and_vessel__artery_" COPASIkey="Reaction_22"/>
    <SBMLMap SBMLid="G6L_astrocytes" COPASIkey="Metabolite_97"/>
    <SBMLMap SBMLid="G6L_neurons" COPASIkey="Metabolite_45"/>
    <SBMLMap SBMLid="GLC_artery" COPASIkey="Metabolite_127"/>
    <SBMLMap SBMLid="GLU_astrocytes" COPASIkey="Metabolite_87"/>
    <SBMLMap SBMLid="GLU_extracellular_space" COPASIkey="Metabolite_119"/>
    <SBMLMap SBMLid="GLU_neurons" COPASIkey="Metabolite_35"/>
    <SBMLMap SBMLid="GND_astrocytes__R01528" COPASIkey="Reaction_46"/>
    <SBMLMap SBMLid="GND_neurons___R01528" COPASIkey="Reaction_45"/>
    <SBMLMap SBMLid="HK_neurons__R01786___HeinrichSchuster" COPASIkey="Reaction_62"/>
    <SBMLMap SBMLid="Hb_OP" COPASIkey="ModelValue_30"/>
    <SBMLMap SBMLid="K_O2__Aubert" COPASIkey="ModelValue_79"/>
    <SBMLMap SBMLid="K_T_GLC_ce__Aubert" COPASIkey="ModelValue_61"/>
    <SBMLMap SBMLid="K_T_GLC_cg__Aubert" COPASIkey="ModelValue_67"/>
    <SBMLMap SBMLid="K_T_GLC_eg__Aubert" COPASIkey="ModelValue_64"/>
    <SBMLMap SBMLid="K_T_GLC_en__Aubert" COPASIkey="ModelValue_65"/>
    <SBMLMap SBMLid="K_T_LAC_ec__Aubert" COPASIkey="ModelValue_76"/>
    <SBMLMap SBMLid="K_T_LAC_gc__Aubert" COPASIkey="ModelValue_75"/>
    <SBMLMap SBMLid="K_T_LAC_ge__Aubert" COPASIkey="ModelValue_74"/>
    <SBMLMap SBMLid="K_T_LAC_ne__Aubert" COPASIkey="ModelValue_73"/>
    <SBMLMap SBMLid="K_m_ATP_ATPase" COPASIkey="ModelValue_55"/>
    <SBMLMap SBMLid="K_m_G6P_GLYS" COPASIkey="ModelValue_35"/>
    <SBMLMap SBMLid="K_m_GLU" COPASIkey="ModelValue_50"/>
    <SBMLMap SBMLid="K_m_GLY" COPASIkey="ModelValue_37"/>
    <SBMLMap SBMLid="K_m_Na_pump" COPASIkey="ModelValue_34"/>
    <SBMLMap SBMLid="LAC_artery" COPASIkey="Metabolite_129"/>
    <SBMLMap SBMLid="LDH_astrocytes_forward__R00703" COPASIkey="Reaction_39"/>
    <SBMLMap SBMLid="LDH_neurons_forward__R00703" COPASIkey="Reaction_40"/>
    <SBMLMap SBMLid="NADPH_astrocytes" COPASIkey="Metabolite_113"/>
    <SBMLMap SBMLid="NADPH_neurons" COPASIkey="Metabolite_59"/>
    <SBMLMap SBMLid="NADPH_oxidase_astrocytes__R07172" COPASIkey="Reaction_58"/>
    <SBMLMap SBMLid="NADPH_oxidase_neurons__R07172" COPASIkey="Reaction_57"/>
    <SBMLMap SBMLid="NADP_astrocytes" COPASIkey="Metabolite_111"/>
    <SBMLMap SBMLid="NADP_neurons" COPASIkey="Metabolite_61"/>
    <SBMLMap SBMLid="NAD_astrocytes" COPASIkey="Metabolite_95"/>
    <SBMLMap SBMLid="NAD_neurons" COPASIkey="Metabolite_43"/>
    <SBMLMap SBMLid="NULL" COPASIkey="ModelValue_56"/>
    <SBMLMap SBMLid="Na__astrocytes" COPASIkey="Metabolite_85"/>
    <SBMLMap SBMLid="Na__extracellular_space" COPASIkey="Metabolite_121"/>
    <SBMLMap SBMLid="Na__neurons" COPASIkey="Metabolite_33"/>
    <SBMLMap SBMLid="O2_artery" COPASIkey="Metabolite_123"/>
    <SBMLMap SBMLid="O2_exchange_capillary_astrocytes" COPASIkey="Reaction_18"/>
    <SBMLMap SBMLid="O2_exchange_capillary_neurons" COPASIkey="Reaction_17"/>
    <SBMLMap SBMLid="O2_transport_function_inkl__volume" COPASIkey="Function_54"/>
    <SBMLMap SBMLid="P6G_astrocytes" COPASIkey="Metabolite_99"/>
    <SBMLMap SBMLid="P6G_neurons" COPASIkey="Metabolite_47"/>
    <SBMLMap SBMLid="PGI_astrocytes__R02740___HS" COPASIkey="Reaction_61"/>
    <SBMLMap SBMLid="PGI_neurons__R02740___HS" COPASIkey="Reaction_63"/>
    <SBMLMap SBMLid="PS_cap_astrocytes__wrt_capillaries" COPASIkey="ModelValue_58"/>
    <SBMLMap SBMLid="PS_cap_astrocytes__wrt_capillaries___Aubert" COPASIkey="ModelValue_77"/>
    <SBMLMap SBMLid="PS_cap_neuron__wrt_capillaries" COPASIkey="ModelValue_59"/>
    <SBMLMap SBMLid="PS_cap_neuron__wrt_capillaries___Aubert" COPASIkey="ModelValue_78"/>
    <SBMLMap SBMLid="R5P_astrocytes" COPASIkey="Metabolite_105"/>
    <SBMLMap SBMLid="R5P_neurons" COPASIkey="Metabolite_53"/>
    <SBMLMap SBMLid="R5P_sink_astrocytes__n_a_" COPASIkey="Reaction_59"/>
    <SBMLMap SBMLid="R5P_sink_neurons__n_a_" COPASIkey="Reaction_60"/>
    <SBMLMap SBMLid="RKI_astrocytes__R01056" COPASIkey="Reaction_49"/>
    <SBMLMap SBMLid="RKI_neurons__R01056" COPASIkey="Reaction_50"/>
    <SBMLMap SBMLid="RPE_astrocytes__R01529" COPASIkey="Reaction_48"/>
    <SBMLMap SBMLid="RPE_neurons__R01529" COPASIkey="Reaction_47"/>
    <SBMLMap SBMLid="RT" COPASIkey="ModelValue_41"/>
    <SBMLMap SBMLid="R_Na_GLU" COPASIkey="ModelValue_48"/>
    <SBMLMap SBMLid="Ru5P_astrocytes" COPASIkey="Metabolite_101"/>
    <SBMLMap SBMLid="Ru5P_neurons" COPASIkey="Metabolite_49"/>
    <SBMLMap SBMLid="S7P_astrocytes" COPASIkey="Metabolite_107"/>
    <SBMLMap SBMLid="S7P_neurons" COPASIkey="Metabolite_55"/>
    <SBMLMap SBMLid="SOL_astrocytes__R02035" COPASIkey="Reaction_44"/>
    <SBMLMap SBMLid="SOL_neurons__R02035" COPASIkey="Reaction_43"/>
    <SBMLMap SBMLid="Sm_g" COPASIkey="ModelValue_31"/>
    <SBMLMap SBMLid="Sm_n" COPASIkey="ModelValue_32"/>
    <SBMLMap SBMLid="TAL_astrocytes__R01827" COPASIkey="Reaction_53"/>
    <SBMLMap SBMLid="TAL_neurons__R01827" COPASIkey="Reaction_54"/>
    <SBMLMap SBMLid="TKL_1_astrocytes__R01641" COPASIkey="Reaction_51"/>
    <SBMLMap SBMLid="TKL_1_neurons__R01641" COPASIkey="Reaction_52"/>
    <SBMLMap SBMLid="TKL_2_astrocytes__R01830" COPASIkey="Reaction_55"/>
    <SBMLMap SBMLid="TKL_2_neurons__R01830" COPASIkey="Reaction_56"/>
    <SBMLMap SBMLid="V_eg_max_GLU" COPASIkey="ModelValue_49"/>
    <SBMLMap SBMLid="V_gn_max_GLU" COPASIkey="ModelValue_51"/>
    <SBMLMap SBMLid="Vm" COPASIkey="ModelValue_40"/>
    <SBMLMap SBMLid="Vmax_ce_GLC__Aubert" COPASIkey="ModelValue_83"/>
    <SBMLMap SBMLid="Vmax_ce_GLC__wrt_capillaries___Aubert" COPASIkey="ModelValue_62"/>
    <SBMLMap SBMLid="Vmax_cg_GLC___Aubert" COPASIkey="ModelValue_84"/>
    <SBMLMap SBMLid="Vmax_cg_GLC__wrt_capillaries___Aubert" COPASIkey="ModelValue_68"/>
    <SBMLMap SBMLid="Vmax_ec_LAC__wrt_extracellular_space___Aubert" COPASIkey="ModelValue_69"/>
    <SBMLMap SBMLid="Vmax_eg_GLC__Aubert_" COPASIkey="ModelValue_85"/>
    <SBMLMap SBMLid="Vmax_eg_GLC__wrt_astrocytes___Aubert_" COPASIkey="ModelValue_63"/>
    <SBMLMap SBMLid="Vmax_eg_GLU__wrt_extracellular_space" COPASIkey="ModelValue_60"/>
    <SBMLMap SBMLid="Vmax_en_GLC___Aubert" COPASIkey="ModelValue_86"/>
    <SBMLMap SBMLid="Vmax_en_GLC__wrt_neurons___Aubert" COPASIkey="ModelValue_66"/>
    <SBMLMap SBMLid="Vmax_f_PGI__Cloutier" COPASIkey="ModelValue_81"/>
    <SBMLMap SBMLid="Vmax_gc_LAC__wrt_astrocytes___Aubert" COPASIkey="ModelValue_70"/>
    <SBMLMap SBMLid="Vmax_ge_LAC__wrt_astrocytes___Aubert" COPASIkey="ModelValue_71"/>
    <SBMLMap SBMLid="Vmax_ne_LAC__wrt_extracellular_space" COPASIkey="ModelValue_57"/>
    <SBMLMap SBMLid="Vmax_ne_LAC__wrt_neurons___Aubert" COPASIkey="ModelValue_72"/>
    <SBMLMap SBMLid="Vmax_r_PGI__Cloutier" COPASIkey="ModelValue_82"/>
    <SBMLMap SBMLid="X5P_astrocytes" COPASIkey="Metabolite_103"/>
    <SBMLMap SBMLid="X5P_neurons" COPASIkey="Metabolite_51"/>
    <SBMLMap SBMLid="ZWF_astrocytes__R02736" COPASIkey="Reaction_41"/>
    <SBMLMap SBMLid="ZWF_neurons__R02736" COPASIkey="Reaction_42"/>
    <SBMLMap SBMLid="_PScap" COPASIkey="ModelValue_88"/>
    <SBMLMap SBMLid="_sf" COPASIkey="ModelValue_87"/>
    <SBMLMap SBMLid="artery" COPASIkey="Compartment_11"/>
    <SBMLMap SBMLid="compartment_1" COPASIkey="Compartment_1"/>
    <SBMLMap SBMLid="compartment_2" COPASIkey="Compartment_3"/>
    <SBMLMap SBMLid="compartment_3" COPASIkey="Compartment_5"/>
    <SBMLMap SBMLid="compartment_4" COPASIkey="Compartment_7"/>
    <SBMLMap SBMLid="dHb" COPASIkey="Metabolite_9"/>
    <SBMLMap SBMLid="delta_F" COPASIkey="ModelValue_23"/>
    <SBMLMap SBMLid="delta_GLY" COPASIkey="ModelValue_36"/>
    <SBMLMap SBMLid="delta_HK" COPASIkey="ModelValue_52"/>
    <SBMLMap SBMLid="f_CBF_dyn" COPASIkey="ModelValue_89"/>
    <SBMLMap SBMLid="facilitated_transport__inkl__Volume" COPASIkey="Function_46"/>
    <SBMLMap SBMLid="facilitated_transport__inkl__Volume____scaled" COPASIkey="Function_58"/>
    <SBMLMap SBMLid="g_Na_astrocytes" COPASIkey="ModelValue_39"/>
    <SBMLMap SBMLid="g_Na_neurons" COPASIkey="ModelValue_38"/>
    <SBMLMap SBMLid="inflow_of_dHb" COPASIkey="Reaction_31"/>
    <SBMLMap SBMLid="is_stimulated" COPASIkey="ModelValue_46"/>
    <SBMLMap SBMLid="k_pump" COPASIkey="ModelValue_33"/>
    <SBMLMap SBMLid="modular_rate_law_for_one_substrate__one_product" COPASIkey="Function_52"/>
    <SBMLMap SBMLid="modular_rate_law_for_two_substrates__two_products" COPASIkey="Function_51"/>
    <SBMLMap SBMLid="nh_O2__Aubert" COPASIkey="ModelValue_80"/>
    <SBMLMap SBMLid="outflow_of_dHb" COPASIkey="Reaction_32"/>
    <SBMLMap SBMLid="parameter_1" COPASIkey="ModelValue_0"/>
    <SBMLMap SBMLid="parameter_10" COPASIkey="ModelValue_9"/>
    <SBMLMap SBMLid="parameter_11" COPASIkey="ModelValue_10"/>
    <SBMLMap SBMLid="parameter_12" COPASIkey="ModelValue_11"/>
    <SBMLMap SBMLid="parameter_14" COPASIkey="ModelValue_12"/>
    <SBMLMap SBMLid="parameter_16" COPASIkey="ModelValue_13"/>
    <SBMLMap SBMLid="parameter_17" COPASIkey="ModelValue_14"/>
    <SBMLMap SBMLid="parameter_18" COPASIkey="ModelValue_15"/>
    <SBMLMap SBMLid="parameter_19" COPASIkey="ModelValue_16"/>
    <SBMLMap SBMLid="parameter_2" COPASIkey="ModelValue_1"/>
    <SBMLMap SBMLid="parameter_20" COPASIkey="ModelValue_17"/>
    <SBMLMap SBMLid="parameter_21" COPASIkey="ModelValue_18"/>
    <SBMLMap SBMLid="parameter_25" COPASIkey="ModelValue_19"/>
    <SBMLMap SBMLid="parameter_26" COPASIkey="ModelValue_20"/>
    <SBMLMap SBMLid="parameter_3" COPASIkey="ModelValue_2"/>
    <SBMLMap SBMLid="parameter_4" COPASIkey="ModelValue_3"/>
    <SBMLMap SBMLid="parameter_43" COPASIkey="ModelValue_21"/>
    <SBMLMap SBMLid="parameter_5" COPASIkey="ModelValue_4"/>
    <SBMLMap SBMLid="parameter_6" COPASIkey="ModelValue_5"/>
    <SBMLMap SBMLid="parameter_7" COPASIkey="ModelValue_6"/>
    <SBMLMap SBMLid="parameter_8" COPASIkey="ModelValue_7"/>
    <SBMLMap SBMLid="parameter_9" COPASIkey="ModelValue_8"/>
    <SBMLMap SBMLid="reaction_10" COPASIkey="Reaction_6"/>
    <SBMLMap SBMLid="reaction_13" COPASIkey="Reaction_7"/>
    <SBMLMap SBMLid="reaction_14" COPASIkey="Reaction_8"/>
    <SBMLMap SBMLid="reaction_17" COPASIkey="Reaction_9"/>
    <SBMLMap SBMLid="reaction_18" COPASIkey="Reaction_10"/>
    <SBMLMap SBMLid="reaction_19" COPASIkey="Reaction_11"/>
    <SBMLMap SBMLid="reaction_2" COPASIkey="Reaction_0"/>
    <SBMLMap SBMLid="reaction_20" COPASIkey="Reaction_12"/>
    <SBMLMap SBMLid="reaction_21" COPASIkey="Reaction_13"/>
    <SBMLMap SBMLid="reaction_22" COPASIkey="Reaction_14"/>
    <SBMLMap SBMLid="reaction_23" COPASIkey="Reaction_15"/>
    <SBMLMap SBMLid="reaction_24" COPASIkey="Reaction_16"/>
    <SBMLMap SBMLid="reaction_5" COPASIkey="Reaction_1"/>
    <SBMLMap SBMLid="reaction_6" COPASIkey="Reaction_2"/>
    <SBMLMap SBMLid="reaction_7" COPASIkey="Reaction_3"/>
    <SBMLMap SBMLid="reaction_8" COPASIkey="Reaction_4"/>
    <SBMLMap SBMLid="reaction_9" COPASIkey="Reaction_5"/>
    <SBMLMap SBMLid="species_1" COPASIkey="Metabolite_11"/>
    <SBMLMap SBMLid="species_10" COPASIkey="Metabolite_71"/>
    <SBMLMap SBMLid="species_11" COPASIkey="Metabolite_21"/>
    <SBMLMap SBMLid="species_12" COPASIkey="Metabolite_23"/>
    <SBMLMap SBMLid="species_13" COPASIkey="Metabolite_73"/>
    <SBMLMap SBMLid="species_14" COPASIkey="Metabolite_75"/>
    <SBMLMap SBMLid="species_15" COPASIkey="Metabolite_25"/>
    <SBMLMap SBMLid="species_16" COPASIkey="Metabolite_29"/>
    <SBMLMap SBMLid="species_17" COPASIkey="Metabolite_77"/>
    <SBMLMap SBMLid="species_18" COPASIkey="Metabolite_27"/>
    <SBMLMap SBMLid="species_19" COPASIkey="Metabolite_79"/>
    <SBMLMap SBMLid="species_2" COPASIkey="Metabolite_13"/>
    <SBMLMap SBMLid="species_20" COPASIkey="Metabolite_81"/>
    <SBMLMap SBMLid="species_21" COPASIkey="Metabolite_31"/>
    <SBMLMap SBMLid="species_22" COPASIkey="Metabolite_83"/>
    <SBMLMap SBMLid="species_23" COPASIkey="Metabolite_1"/>
    <SBMLMap SBMLid="species_24" COPASIkey="Metabolite_3"/>
    <SBMLMap SBMLid="species_25" COPASIkey="Metabolite_5"/>
    <SBMLMap SBMLid="species_26" COPASIkey="Metabolite_7"/>
    <SBMLMap SBMLid="species_27" COPASIkey="Metabolite_115"/>
    <SBMLMap SBMLid="species_28" COPASIkey="Metabolite_117"/>
    <SBMLMap SBMLid="species_3" COPASIkey="Metabolite_15"/>
    <SBMLMap SBMLid="species_4" COPASIkey="Metabolite_63"/>
    <SBMLMap SBMLid="species_5" COPASIkey="Metabolite_65"/>
    <SBMLMap SBMLid="species_6" COPASIkey="Metabolite_67"/>
    <SBMLMap SBMLid="species_7" COPASIkey="Metabolite_17"/>
    <SBMLMap SBMLid="species_8" COPASIkey="Metabolite_69"/>
    <SBMLMap SBMLid="species_9" COPASIkey="Metabolite_19"/>
    <SBMLMap SBMLid="stimulus" COPASIkey="ModelValue_90"/>
    <SBMLMap SBMLid="t_0" COPASIkey="ModelValue_24"/>
    <SBMLMap SBMLid="t_1" COPASIkey="ModelValue_26"/>
    <SBMLMap SBMLid="t_end" COPASIkey="ModelValue_25"/>
    <SBMLMap SBMLid="t_stim_tp" COPASIkey="ModelValue_45"/>
    <SBMLMap SBMLid="tau_v" COPASIkey="ModelValue_29"/>
    <SBMLMap SBMLid="vATPase" COPASIkey="Function_42"/>
    <SBMLMap SBMLid="vGLU_eg" COPASIkey="Reaction_29"/>
    <SBMLMap SBMLid="vGLU_eg__inkl__Volumes" COPASIkey="Function_47"/>
    <SBMLMap SBMLid="vGLU_gn" COPASIkey="Reaction_30"/>
    <SBMLMap SBMLid="vGLU_gn__inkl__Volume" COPASIkey="Function_48"/>
    <SBMLMap SBMLid="vGLU_ne" COPASIkey="Reaction_28"/>
    <SBMLMap SBMLid="vGLU_ne__inkl__Volume" COPASIkey="Function_49"/>
    <SBMLMap SBMLid="vHK__HS" COPASIkey="Function_57"/>
    <SBMLMap SBMLid="vLEAK_Na_astrocytes" COPASIkey="Reaction_26"/>
    <SBMLMap SBMLid="vLEAK_Na_inkl__Volume" COPASIkey="Function_55"/>
    <SBMLMap SBMLid="vLEAK_Na_neurons" COPASIkey="Reaction_25"/>
    <SBMLMap SBMLid="vMITO2__inkl__Volumes" COPASIkey="Function_59"/>
    <SBMLMap SBMLid="vPFK" COPASIkey="Function_45"/>
    <SBMLMap SBMLid="vPGK" COPASIkey="Function_44"/>
    <SBMLMap SBMLid="vPK" COPASIkey="Function_43"/>
    <SBMLMap SBMLid="vPUMP_astrocytes" COPASIkey="Reaction_24"/>
    <SBMLMap SBMLid="vPUMP_neurons" COPASIkey="Reaction_23"/>
    <SBMLMap SBMLid="vPUMP_volume_dependent" COPASIkey="Function_56"/>
    <SBMLMap SBMLid="vSTIM" COPASIkey="Reaction_27"/>
    <SBMLMap SBMLid="vStim__with_volume" COPASIkey="Function_50"/>
    <SBMLMap SBMLid="v_stim" COPASIkey="ModelValue_47"/>
    <SBMLMap SBMLid="vdHb_in" COPASIkey="Function_40"/>
    <SBMLMap SBMLid="vdHb_out" COPASIkey="Function_41"/>
    <SBMLMap SBMLid="venous_balloon" COPASIkey="Compartment_9"/>
    <SBMLMap SBMLid="vn_1_tp" COPASIkey="ModelValue_43"/>
    <SBMLMap SBMLid="vn_2_tp" COPASIkey="ModelValue_44"/>
  </SBMLReference>
  <ListOfUnitDefinitions>
    <UnitDefinition key="Unit_0" name="meter" symbol="m">
      <Expression>
        m
      </Expression>
    </UnitDefinition>
    <UnitDefinition key="Unit_2" name="second" symbol="s">
      <Expression>
        s
      </Expression>
    </UnitDefinition>
  </ListOfUnitDefinitions>
</COPASI>
