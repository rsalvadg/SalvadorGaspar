package com.telefonica.gdre.srv.nuc.adapmodel.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.eclipse.persistence.jpa.jpql.parser.NewValueBNF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tangosol.run.xml.ArrayAdapter;
import com.telefonica.gdre.dao.adaptationresource.dto.FindAdapParametersByAdapCodeIdNaDaoOut;
import com.telefonica.gdre.dao.adaptationresource.dto.GetADCOREQMNSEQDaoOut;
import com.telefonica.gdre.dao.adaptationresource.dto.GetAdapOperationDaoOut;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsoaAdOpTranRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsoaAdTaskStepSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsodAdNetSeTypeSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsodAdNetSerSpReTyRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsodAdapFaSpecTypeRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsodAdapOperationTypeRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsodAdapXmlTypeRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsodLockVoiceTypeRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdFacValueSpecRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdNetLayerSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapAccSpecRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapEquipRoleSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapFacSpecRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapNetSerSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapNetTrailSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapOperationRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapParameterRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapSerLineSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapTrailSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapTransportSpecRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsopAdapXmlRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorANetSeHEquRoSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdAcSpURSpChValRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdAccFSpHasRcvRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdAccFacSpHasEiRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdAccSpULogRSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdAccUseEqRoSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdAccUseFacSpecRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdEqRoFSpHChVaRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdEqRoFSpHEiRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdEqRoRelIsTriByRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdEqRoUseFacSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFVaSpTrtoGaRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFVaSpTrtoRscRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFVaSpTrtoSscRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFaPsChVaRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFaSHResSRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFaVaPsChVaRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFacSpTrSeSCvRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFacSpTrtoEiRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdFacSpTrtoRscRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNSeReqTrailSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNSeSpUseTRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNSerTrRfsSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNSspFacHRcvRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNSspUseFacSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetLaySpExeOpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetSSPTbyRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetSSpSubyRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetSSpTbyRecRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetSspFHEiRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetSspFacHScvRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetTrFSpHEiRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetTrFSpHRcvRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetTrUseEqRoRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNetTrUseFaSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdNssHasFacValRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdSeliSpUseNetseRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdSerLiUseAcSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdSlSpFHEiRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdSlSpUseFaSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdTrailSpULrSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdapFacSpRelRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdapFacVaSusbyRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdapNetSeSpRelRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdapNssIsTrigByRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorAdapSeLiTrRfsSpRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorNeLayHEqRoleRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorNeLayerHNetServRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorNetLayHasSeLineRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorNetLayUseAcRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorNetLayerHTrailRepository;
import com.telefonica.gdre.dao.adaptationresource.repository.RcsorNetLayerHasFacRepository;
import com.telefonica.gdre.model.RcsoaAdOpTran;
import com.telefonica.gdre.model.RcsoaAdOpTranPK;
import com.telefonica.gdre.model.RcsoaAdTaskStepSp;
import com.telefonica.gdre.model.RcsodAdNetSeTypeSp;
import com.telefonica.gdre.model.RcsodAdNetSerSpReTy;
import com.telefonica.gdre.model.RcsodAdapFaSpecType;
import com.telefonica.gdre.model.RcsodAdapOperationType;
import com.telefonica.gdre.model.RcsodAdapXmlType;
import com.telefonica.gdre.model.RcsodLockVoiceType;
import com.telefonica.gdre.model.RcsopAdFacValueSpec;
import com.telefonica.gdre.model.RcsopAdNetLayerSp;
import com.telefonica.gdre.model.RcsopAdapAccSpec;
import com.telefonica.gdre.model.RcsopAdapEquipRoleSp;
import com.telefonica.gdre.model.RcsopAdapFacSpec;
import com.telefonica.gdre.model.RcsopAdapNetSerSp;
import com.telefonica.gdre.model.RcsopAdapNetTrailSp;
import com.telefonica.gdre.model.RcsopAdapOperation;
import com.telefonica.gdre.model.RcsopAdapParameter;
import com.telefonica.gdre.model.RcsopAdapSerLineSp;
import com.telefonica.gdre.model.RcsopAdapTrailSp;
import com.telefonica.gdre.model.RcsopAdapTransportSpec;
import com.telefonica.gdre.model.RcsopAdapXml;
import com.telefonica.gdre.model.RcsorANetSeHEquRoSp;
import com.telefonica.gdre.model.RcsorAdAcSpURSpChVal;
import com.telefonica.gdre.model.RcsorAdAcSpURSpChValPK;
import com.telefonica.gdre.model.RcsorAdAccSpULogRSp;
import com.telefonica.gdre.model.RcsorAdAccSpULogRSpPK;
import com.telefonica.gdre.model.RcsorAdAccUseEqRoSp;
import com.telefonica.gdre.model.RcsorAdAccUseFacSpec;
import com.telefonica.gdre.model.RcsorAdEqRoRelIsTriBy;
import com.telefonica.gdre.model.RcsorAdEqRoRelIsTriByPK;
import com.telefonica.gdre.model.RcsorAdEqRoUseFacSp;
import com.telefonica.gdre.model.RcsorAdFVaSpTrtoGa;
import com.telefonica.gdre.model.RcsorAdFVaSpTrtoGaPK;
import com.telefonica.gdre.model.RcsorAdFVaSpTrtoRsc;
import com.telefonica.gdre.model.RcsorAdFVaSpTrtoRscPK;
import com.telefonica.gdre.model.RcsorAdFVaSpTrtoSsc;
import com.telefonica.gdre.model.RcsorAdFVaSpTrtoSscPK;
import com.telefonica.gdre.model.RcsorAdFaPsChVa;
import com.telefonica.gdre.model.RcsorAdFaPsChVaPK;
import com.telefonica.gdre.model.RcsorAdFaSHResS;
import com.telefonica.gdre.model.RcsorAdFaSHResSPK;
import com.telefonica.gdre.model.RcsorAdFaVaPsChVa;
import com.telefonica.gdre.model.RcsorAdFaVaPsChVaPK;
import com.telefonica.gdre.model.RcsorAdFacSpTrSeSCv;
import com.telefonica.gdre.model.RcsorAdFacSpTrSeSCvPK;
import com.telefonica.gdre.model.RcsorAdFacSpTrtoEi;
import com.telefonica.gdre.model.RcsorAdFacSpTrtoEiPK;
import com.telefonica.gdre.model.RcsorAdFacSpTrtoRsc;
import com.telefonica.gdre.model.RcsorAdFacSpTrtoRscPK;
import com.telefonica.gdre.model.RcsorAdNSeReqTrailSp;
import com.telefonica.gdre.model.RcsorAdNSeSpUseT;
import com.telefonica.gdre.model.RcsorAdNSerTrRfsSp;
import com.telefonica.gdre.model.RcsorAdNSerTrRfsSpPK;
import com.telefonica.gdre.model.RcsorAdNSspUseFacSp;
import com.telefonica.gdre.model.RcsorAdNetLaySpExeOp;
import com.telefonica.gdre.model.RcsorAdNetSSPTby;
import com.telefonica.gdre.model.RcsorAdNetSSPTbyPK;
import com.telefonica.gdre.model.RcsorAdNetSSpSuby;
import com.telefonica.gdre.model.RcsorAdNetSSpSubyPK;
import com.telefonica.gdre.model.RcsorAdNetSSpTbyRec;
import com.telefonica.gdre.model.RcsorAdNetSSpTbyRecPK;
import com.telefonica.gdre.model.RcsorAdNetTrUseEqRo;
import com.telefonica.gdre.model.RcsorAdNetTrUseFaSp;
import com.telefonica.gdre.model.RcsorAdNssHasFacVal;
import com.telefonica.gdre.model.RcsorAdNssHasFacValPK;
import com.telefonica.gdre.model.RcsorAdSeliSpUseNetse;
import com.telefonica.gdre.model.RcsorAdSerLiUseAcSp;
import com.telefonica.gdre.model.RcsorAdSlSpUseFaSp;
import com.telefonica.gdre.model.RcsorAdTrailSpULrSp;
import com.telefonica.gdre.model.RcsorAdTrailSpULrSpPK;
import com.telefonica.gdre.model.RcsorAdapFacSpRel;
import com.telefonica.gdre.model.RcsorAdapFacVaSusby;
import com.telefonica.gdre.model.RcsorAdapFacVaSusbyPK;
import com.telefonica.gdre.model.RcsorAdapNetSeSpRel;
import com.telefonica.gdre.model.RcsorAdapNssIsTrigBy;
import com.telefonica.gdre.model.RcsorAdapNssIsTrigByPK;
import com.telefonica.gdre.model.RcsorAdapSeLiTrRfsSp;
import com.telefonica.gdre.model.RcsorAdapSeLiTrRfsSpPK;
import com.telefonica.gdre.model.RcsorNeLayHEqRole;
import com.telefonica.gdre.model.RcsorNeLayHEqRolePK;
import com.telefonica.gdre.model.RcsorNeLayerHNetServ;
import com.telefonica.gdre.model.RcsorNeLayerHNetServPK;
import com.telefonica.gdre.model.RcsorNetLayHasSeLine;
import com.telefonica.gdre.model.RcsorNetLayHasSeLinePK;
import com.telefonica.gdre.model.RcsorNetLayUseAc;
import com.telefonica.gdre.model.RcsorNetLayUseAcPK;
import com.telefonica.gdre.model.RcsorNetLayerHTrail;
import com.telefonica.gdre.model.RcsorNetLayerHTrailPK;
import com.telefonica.gdre.model.RcsorNetLayerHasFac;
import com.telefonica.gdre.model.RcsorNetLayerHasFacPK;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.deletewholeadaptationmodel.DeleteWholeAdaptationModel_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.deletewholeadaptationmodel.DeleteWholeAdaptationModel_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.findadapparametersbyadapcode.AdapParameters_DTO_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.findadapparametersbyadapcode.AdapParameters_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.findadapparametersbyadapcode.FindAdapParametersByAdapCode_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.findadapparametersbyadapcode.FindAdapParametersByAdapCode_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.AdapNetLaySpExecutesOp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.AdapNetworkLayerSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.AdapOperationType_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.AdapOperation_DTO_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.AdapOperation_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.GetAdapOperation_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.GetAdapOperation_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.ItemWorkflowSpecTask_DTO_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapoperation.LockVoiceType_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.AdapXMLType_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.GetAdapXML_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.GetAdapXML_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.OrchestrationPlanTask_DTO_OUT;

import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapAccessSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapAccessUseEquipRoleSp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapAccessUseFacSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapEquipRoleUseFacilitySp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapEquipmentRoleSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetSSpUseFacilitySp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetServHasEquipmRoleSp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetServRequiresTrailSp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetTrailSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetTrailUseEquipRole_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetTrailUseFacilitySp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetwSerSpRelType_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpecRel_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapSLSpUseFacilitySp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapSerLineUseAccessSp_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapServLineSpUseNetServ_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapServiceLineSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapTaskStepSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapTrailSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapTransportSpec_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.GetWholeAdaptationModelByAdapNetworkLayerSpec_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.GetWholeAdaptationModelByAdapNetworkLayerSpec_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertadapxml.AdapXML_DTO_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertadapxml.InsertAdapXML_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertadapxml.InsertAdapXML_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapEquipRoleUseFacilitySp_DTO_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapFacilitySpec_DTO_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.InsertWholeAdaptationModel_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.InsertWholeAdaptationModel_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.updateadapparametersbyadapcode.UpdateAdapParametersByAdapCode_IN;
import com.telefonica.gdre.srv.nuc.adapmodel.msg.updateadapparametersbyadapcode.UpdateAdapParametersByAdapCode_OUT;
import com.telefonica.gdre.srv.nuc.adapmodel.service.AdapModelService;
import com.telefonica.tran.comarq.cc.cabecera.TE_Cabecera;
import com.telefonica.tran.comarq.cc.error.TE_Excepcion;
import com.telefonica.tran.comarq.cc.metadatos.TE_Metadatos;

//@Validated
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { TE_Excepcion.class })
@Service
public class AdapModelServiceImpl implements AdapModelService {

	/**
	 * Logger de la clase
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdapModelServiceImpl.class);

	@Autowired
	private RcsopAdapOperationRepository rcsopAdapOperationRepository;
	@Autowired
	private RcsoaAdOpTranRepository rcsoaAdOpTranRepository;
	@Autowired
	private RcsorAdSlSpUseFaSpRepository rcsorAdSlSpUseFaSpRepository;
	@Autowired
	private RcsopAdapFacSpecRepository rcsopAdapFacSpecRepository;
	@Autowired
	private RcsodAdapFaSpecTypeRepository rcsodAdapFaSpecTypeRepository;
	@Autowired
	private RcsorAdapFacSpRelRepository rcsorAdapFacSpRelRepository;
	@Autowired
	private RcsorAdSeliSpUseNetseRepository rcsorAdSeliSpUseNetseRepository;
	@Autowired
	private RcsopAdapNetSerSpRepository rcsopAdapNetSerSpRepository;
	@Autowired
	private RcsodAdNetSeTypeSpRepository rcsodAdNetSeTypeSpRepository;
	@Autowired
	private RcsorAdapNetSeSpRelRepository rcsorAdapNetSeSpRelRepository;
	@Autowired
	private RcsorANetSeHEquRoSpRepository rcsorANetSeHEquRoSpRepository;
	@Autowired
	private RcsopAdapEquipRoleSpRepository rcsopAdapEquipRoleSpRepository;
	@Autowired
	private RcsorAdEqRoUseFacSpRepository rcsorAdEqRoUseFacSpRepository;
	@Autowired
	private RcsorAdNSeReqTrailSpRepository rcsorAdNSeReqTrailSpRepository;
	@Autowired
	private RcsopAdapTrailSpRepository rcsopAdapTrailSpRepository;
	@Autowired
	private RcsopAdapNetTrailSpRepository rcsopAdapNetTrailSpRepository;
	@Autowired
	private RcsorAdNetTrUseFaSpRepository rcsorAdNetTrUseFaSpRepository;
	@Autowired
	private RcsorAdNetTrUseEqRoRepository rcsorAdNetTrUseEqRoRepository;
	@Autowired
	private RcsopAdapTransportSpecRepository rcsopAdapTransportSpecRepository;
	@Autowired
	private RcsopAdapAccSpecRepository rcsopAdapAccSpecRepository;
	@Autowired
	private RcsorAdAccUseEqRoSpRepository rcsorAdAccUseEqRoSpRepository;
	@Autowired
	private RcsorAdAccUseFacSpecRepository rcsorAdAccUseFacSpecRepository;
	@Autowired
	private RcsorAdSerLiUseAcSpRepository rcsorAdSerLiUseAcSpRepository;
	@Autowired
	private RcsopAdapParameterRepository rcsopAdapParameterRepository;
	@Autowired
	private RcsorNetLayHasSeLineRepository rcsorNetLayHasSeLineRepository;
	@Autowired
	private RcsopAdNetLayerSpRepository rcsopAdNetLayerSpRepository;

	@Autowired
	private RcsodAdapOperationTypeRepository rcsodAdapOperationTypeRepository;

	@Autowired
	private RcsodLockVoiceTypeRepository rcsodLockVoiceTypeRepository;

	@Autowired
	private RcsorAdNetLaySpExeOpRepository rcsorAdNetLaySpExeOpRepository;

	@Autowired
	private RcsopAdapSerLineSpRepository rcsopAdapSerLineSpRepository;

	@Autowired
	private RcsorAdapSeLiTrRfsSpRepository rcsorAdapSeLiTrRfsSpRepository;

	@Autowired
	private RcsorAdNetSSpSubyRepository rcsorAdNetSSpSubyRepository;

	@Autowired
	private RcsorAdNSerTrRfsSpRepository rcsorAdNSerTrRfsSpRepository;

	@Autowired
	private RcsorAdNetSSPTbyRepository rcsorAdNetSSpTbyRepository;

	@Autowired
	private RcsorAdNetSSpTbyRecRepository rcsorAdNetSSpTbyRecRepository;

	@Autowired
	private RcsorAdNSspUseFacSpRepository rcsorAdNSspUseFacSpRepository;
	@Autowired
	private RcsorAdNSeSpUseTRepository rcsorAdNSeSpUseTRepository;
	@Autowired
	private RcsorNeLayerHNetServRepository rcsorNeLayerHNetServRepository;
	@Autowired
	private RcsorAdapNssIsTrigByRepository rcsorAdapNssIsTrigByRepository;
	@Autowired
	private RcsorNetLayerHTrailRepository rcsorNetLayerHTrailRepository;
	@Autowired
	private RcsorNetLayUseAcRepository rcsorNetLayUseAcRepository;
	@Autowired
	private RcsorNetLayerHasFacRepository rcsorNetLayerHasFacRepository;
	@Autowired
	private RcsorNeLayHEqRoleRepository rcsorNeLayHEqRoleRepository;
	@Autowired
	private RcsorAdSlSpFHEiRepository rcsorAdSlSpFHEiRepository;
	@Autowired
	private RcsorAdNetSspFacHScvRepository rcsorAdNetSspFacHScvRepository;
	@Autowired
	private RcsorAdNetSspFHEiRepository rcsorAdNetSspFHEiRepository;
	@Autowired
	private RcsorAdNSspFacHRcvRepository rcsorAdNSspFacHRcvRepository;
	@Autowired
	private RcsorAdNssHasFacValRepository rcsorAdNssHasFacValRepository;
	@Autowired
	private RcsorAdNetTrFSpHEiRepository rcsorAdNetTrFSpHEiRepository;
	@Autowired
	private RcsorAdNetTrFSpHRcvRepository rcsorAdNetTrFSpHRcvRepository;
	@Autowired
	private RcsorAdTrailSpULrSpRepository rcsorAdTrailSpULrSpRepository;
	@Autowired
	private RcsorAdAccFacSpHasEiRepository rcsorAdAccFacSpHasEiRepository;
	@Autowired
	private RcsorAdAccFSpHasRcvRepository rcsorAdAccFSpHasRcvRepository;
	@Autowired
	private RcsorAdEqRoFSpHChVaRepository rcsorAdEqRoFSpHChVaRepository;
	@Autowired
	private RcsorAdEqRoFSpHEiRepository rcsorAdEqRoFSpHEiRepository;
	@Autowired
	private RcsorAdEqRoRelIsTriByRepository rcsorAdEqRoRelIsTriByRepository;

	@Autowired
	private RcsodAdNetSerSpReTyRepository rcsodAdNetSerSpReTyRepository;

	@Autowired
	private RcsoaAdTaskStepSpRepository rcsoaAdTaskStepSpRepository;

	@Autowired
	private RcsorAdAcSpURSpChValRepository rcsorAdAcSpURSpChValRepository;

	@Autowired
	private RcsorAdAccSpULogRSpRepository rcsorAdAccSpULogRSpRepository;

	@Autowired
	private RcsorAdFaSHResSRepository rcsorAdFaSHResSRepository;

	@Autowired
	private RcsorAdFaPsChVaRepository rcsorAdFaPsChVaRepository;

	@Autowired
	private RcsorAdFacSpTrtoRscRepository rcsorAdFacSpTrtoRscRepository;

	@Autowired
	private RcsorAdFacSpTrSeSCvRepository rcsorAdFacSpTrSeSCvRepository;

	@Autowired
	private RcsorAdFacSpTrtoEiRepository rcsorAdFacSpTrtoEiRepository;

	@Autowired
	private RcsopAdFacValueSpecRepository rcsopAdFacValueSpecRepository;

	@Autowired
	private RcsorAdFVaSpTrtoGaRepository rcsorAdFVaSpTrtoGaRepository;

	@Autowired
	private RcsorAdapFacVaSusbyRepository rcsorAdapFacVaSusbyRepository;

	@Autowired
	private RcsorAdFaVaPsChVaRepository rcsorAdFaVaPsChVaRepository;

	@Autowired
	private RcsorAdFVaSpTrtoSscRepository rcsorAdFVaSpTrtoSscRepository;

	@Autowired
	private RcsorAdFVaSpTrtoRscRepository rcsorAdFVaSpTrtoRscRepository;

	@Autowired
	private RcsopAdapXmlRepository rcsopAdapXmlRepository;

	@Autowired
	private RcsodAdapXmlTypeRepository rcsodAdapXmlTypeRepository;

	/**
	 * Descripcion del GDRE-E-000101
	 */
	private static final String DESCRIPTION_000101 = "Error de Acceso a BD";
	/**
	 * Error GDRE-E-000101
	 */
	private static final String ERROR_000101 = "GDRE-E-000101";

	/**
	 * Descripcion del GDRE-E-000102
	 */
	private static final String DESCRIPTION_000102 = "Datos Inexistentes en BBDD";
	/**
	 * Error GDRE-E-000102
	 */
	private static final String ERROR_000102 = "GDRE-E-000102";

	/**
	 * Descripcion del GDRE-E-000112
	 */
	private static final String DESCRIPTION_000112 = "Error de acceso o espacio en Base de datos al insertar el XML";
	/**
	 * Error GDRE-E-000112
	 */
	private static final String ERROR_000112 = "GDRE-E-000112";

	/**
	 * Name Sequence RCSO_ADCOREQMN_SEQ
	 */
	private static final String sequence = "RCSO_ADCOREQMN_SEQ";

	/**
	 * Name Sequence RCSO_ADFACVALUESPEC_SEQ
	 */
	private static final String sequence39 = "RCSO_ADFACVALUESPEC_SEQ";

	/**
	 * Name Vacio
	 */
	private static final String vacio = "VACIA";

	/**
	 * Constante VERSION_CARGA
	 */
	private static final String VERSION_CARGA = "VERSION_CARGA";

	/**
	 * permite borrar el modelo de datos de adaptación. Elmina todas las
	 * entidades del modelo excepto las entidades AdapNetworkLayerSpec, AdapXML
	 * y AdapXMLType. Con esto no perdemos los XML asociados a Layer previos al
	 * borrado.
	 * 
	 * @param DeleteWholeAdaptationModel_IN
	 *            deletewholeadaptationmodel_in
	 * @param TE_Cabecera
	 *            te_Cabecera
	 * @param TE_Metadatos
	 *            te_Metadatos
	 * @return DeleteWholeAdaptationModel_OUT
	 * @throws TE_Excepcion
	 */

	public DeleteWholeAdaptationModel_OUT deleteWholeAdaptationModel(
			DeleteWholeAdaptationModel_IN deletewholeadaptationmodel_in, TE_Cabecera te_Cabecera,
			TE_Metadatos te_Metadatos) throws TE_Excepcion {
		LOGGER.info("INICIO AdapModelServiceImpl.deleteWholeAdaptationModel");
		if (deletewholeadaptationmodel_in == null) {
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}

		DeleteWholeAdaptationModel_OUT out = new DeleteWholeAdaptationModel_OUT();
		try {

			// Borrado de rcsopAdapParameterRepository (RCSOP_ADAP_PARAMETERS)
			RcsopAdapParameter rcsopAdapParameterRepositoryOut = rcsopAdapParameterRepository.findOne(VERSION_CARGA);
			if (rcsopAdapParameterRepositoryOut != null) {
				// Se borra todo
				rcsopAdapParameterRepository.deleteAll();
				rcsopAdapParameterRepository.flush();

				// Se guarda el version_info
				Timestamp fechaActual = new Timestamp(new Date().getTime());
				BigDecimal usuario = BigDecimal.ZERO;
				if (te_Cabecera != null) {
					usuario = new BigDecimal(te_Cabecera.getIdUsuario());
				}
				List<RcsopAdapParameter> listaAdapParameter = new ArrayList<RcsopAdapParameter>();
				RcsopAdapParameter param = new RcsopAdapParameter();
				param.setAdpaIdAdapCode(rcsopAdapParameterRepositoryOut.getAdpaIdAdapCode());
				param.setAdpaNaValue(rcsopAdapParameterRepositoryOut.getAdpaNaValue());
				param.setAdpaDsAdapParameters(rcsopAdapParameterRepositoryOut.getAdpaDsAdapParameters());
				param.setUserIdCreatorParty(usuario);
				param.setAudiTiCreation(fechaActual);
				listaAdapParameter.add(param);
				rcsopAdapParameterRepository.save(listaAdapParameter);
				rcsopAdapParameterRepository.flush();

			}

			// Borrado de rcsopAdapSerLineSpRepository
			// primero relaciones con:
			rcsorAdapSeLiTrRfsSpRepository.deleteAll(); // RCSOR_ADAP_SE_LI_TR_RFS_SP
			// Nueva
			rcsorNetLayHasSeLineRepository.deleteAll(); // RCSOR_NET_LAY_HAS_SE_LINE
			// Nueva
			rcsorAdSeliSpUseNetseRepository.deleteAll(); // RCSOR_AD_SELI_SP_USE_NETSE
			// Común con
			// RCSOP_ADAP_NET_SER_SP
			rcsorAdSlSpFHEiRepository.deleteAll(); // RcsorAdSlSpFHEi
			// RCSOR_AD_SL_SP_F_H_EI
			rcsorAdSlSpUseFaSpRepository.deleteAll(); // RCSOR_AD_SL_SP_USE_FA_SP
			rcsorAdSerLiUseAcSpRepository.deleteAll(); // RCSOR_AD_SER_LI_USE_AC_SP
			rcsopAdapSerLineSpRepository.deleteAll(); // RCSOP_ADAP_SER_LINE_SP
			// Borrado de RcsopAdapNetSerSp
			// Primero relaciones con:
			rcsorAdapNetSeSpRelRepository.deleteAll(); // RCSOR_ADAP_NET_SE_SP_REL
			rcsorAdNetSSpTbyRepository.deleteAll(); // RCSOR_AD_NET_S_S_P_TBY
			rcsorAdNetSSpTbyRecRepository.deleteAll(); // RCSOR_AD_NET_S_SP_TBY_REC
			rcsorAdNetSspFacHScvRepository.deleteAll(); // CSOR_AD_NET_SSP_FAC_H_SCV
			rcsorAdNetSspFHEiRepository.deleteAll(); // RCSOR_AD_NET_SSP_F_H_EI
			rcsorAdNSspFacHRcvRepository.deleteAll(); // RCSOR_AD_N_SSP_FAC_H_RCV
			rcsorAdNSspUseFacSpRepository.deleteAll(); // RCSOR_AD_N_SSP_USE_FAC_SP
			rcsorAdNssHasFacValRepository.deleteAll(); // RCSOR_AD_NSS_HAS_FAC_VALS
			rcsorANetSeHEquRoSpRepository.deleteAll(); // RCSOR_A_NET_SE_H_EQU_RO_SP
			rcsorAdNSerTrRfsSpRepository.deleteAll(); // RCSOR_AD_N_SER_TR_RFS_SP.
			rcsorAdNetSSpSubyRepository.deleteAll(); // RCSOR_AD_NET_S_SP_SUBY.
			rcsorAdNSeSpUseTRepository.deleteAll(); // RCSOR_AD_N_SE_SP_USE_T.
			rcsorAdNSeReqTrailSpRepository.deleteAll(); // RCSOR_AD_N_SE_REQ_TRAIL_SP
			rcsorNeLayerHNetServRepository.deleteAll(); // RCSOR_NE_LAYER_H_NET_SERV.

			// Para poder borrar la siguiente tabla, debemos consultar los
			// registros de Adap_Operation que se pueden borrar y que no guardan
			// relación con rcsorAdapNssIsTrigBy.
			// Consultamos los registros que se pueden borrar de la tabla
			// AdapOperation
			List<RcsopAdapOperation> idsAdapOp = rcsopAdapOperationRepository.findOperationWithoutXml();
			// List<RcsopAdapXml> listAdapXmls =
			// rcsopAdapXmlRepository.findAll();
			List<RcsopAdapOperation> listaAdapOperation = rcsopAdapOperationRepository.findAll();
			listaAdapOperation.removeAll(idsAdapOp);

			List<Long> idsTypeOperationNoBorrar = new ArrayList<Long>();
			for (RcsopAdapOperation opNotDelete : listaAdapOperation) {
				if (!idsTypeOperationNoBorrar
						.contains(opNotDelete.getRcsodAdapOperationType().getAoptIdAdapOperationType())) {
					idsTypeOperationNoBorrar.add(opNotDelete.getRcsodAdapOperationType().getAoptIdAdapOperationType());
				}

			}
			// Creamos una lista de ids para eliminar los registros de las
			// tablas que apuntan a registros de AdapOperation, los cuales van a
			// ser eliminados.
			List<Long> idsBorrar = new ArrayList<Long>(idsAdapOp.size());
			List<Long> idsAdapTypeBorrar = new ArrayList<Long>();
			for (RcsopAdapOperation element : idsAdapOp) {
				idsBorrar.add(element.getAdopIdAdapOperation());
				if (!idsAdapTypeBorrar.contains(element.getRcsodAdapOperationType().getAoptIdAdapOperationType())) {
					idsAdapTypeBorrar.add(element.getRcsodAdapOperationType().getAoptIdAdapOperationType());
				}
			}

			// List<RcsorAdapNssIsTrigBy> idsAdapNssIsTrigBy =
			// rcsorAdapNssIsTrigByRepository
			// .getAdapNssIsTrigByIdAdapOperation(idsBorrar);
			rcsorAdapNssIsTrigByRepository.deleteAll();// (idsAdapNssIsTrigBy);
			// //
			// RCSOR_ADAP_NSS_IS_TRIG_BY
			rcsopAdapNetSerSpRepository.deleteAll(); // RCSOP_ADAP_NET_SER_SP
			// Borrado de RcsopAdapNetTrailSp
			// Primero relaciones con:
			rcsorAdNetTrFSpHEiRepository.deleteAll(); // RCSOR_AD_NET_TR_F_SP_H_EI
			rcsorAdNetTrFSpHRcvRepository.deleteAll(); // RCSOR_AD_NET_TR_F_SP_H_RCV
			rcsorAdNetTrUseFaSpRepository.deleteAll(); // RCSOR_AD_NET_TR_USE_FA_SP
			rcsorAdNetTrUseEqRoRepository.deleteAll(); // RCSOR_AD_NET_TR_USE_EQ_RO
			rcsopAdapNetTrailSpRepository.deleteAll(); // RCSOP_ADAP_NET_TRAIL_SP
			// Borrado de RcsopAdapTrailSp
			// Primero relaciones con:
			rcsorNetLayerHTrailRepository.deleteAll();// RCSOR_NET_LAYER_H_TRAIL.
			rcsorAdTrailSpULrSpRepository.deleteAll();// RCSOR_AD_TRAIL_SP_U_LR_SP
			rcsopAdapTrailSpRepository.deleteAll(); // RCSOP_ADAP_TRAIL_SP
			// Borrado de AdapAccessSpec
			// Primero relaciones con:
			rcsorAdAccSpULogRSpRepository.deleteAll(); // RCSOR_AD_ACC_SP_U_LOG_R_SP.
			rcsorNetLayUseAcRepository.deleteAll(); // RCSOR_NET_LAY_USE_AC
			rcsorAdAccUseEqRoSpRepository.deleteAll(); // RCSOR_AD_ACC_USE_EQ_RO_SP
			rcsorAdAccFacSpHasEiRepository.deleteAll(); // RCSOR_AD_ACC_FAC_SP_HAS_EI
			rcsorAdAccFSpHasRcvRepository.deleteAll(); // RCSOR_AD_ACC_F_SP_HAS_RCV
			rcsorAdAccUseFacSpecRepository.deleteAll(); // RCSOR_AD_ACC_USE_FAC_SPEC
			rcsorAdAcSpURSpChValRepository.deleteAll(); // RCSOR_AD_AC_SP_U_R_SP_CH_VAL
			rcsopAdapAccSpecRepository.deleteAll(); // RCSOP_ADAP_ACC_SPEC
			// Borrado de RcsopAdapTransportSpecRepository
			rcsopAdapTransportSpecRepository.deleteAll(); // RCSOP_ADAP_TRANSPORT_SPEC.
			// Es única
			// Borrado de RcsopAdapFacSpecRepository
			// Primero relaciones con:
			rcsorAdFacSpTrSeSCvRepository.deleteAll(); // RCSOR_AD_FAC_SP_TR_SE_S_CV
			rcsorAdFacSpTrtoEiRepository.deleteAll(); // RCSOR_AD_FAC_SP_TRTO_EIS
			rcsorNetLayerHasFacRepository.deleteAll(); // RCSOR_NET_LAYER_HAS_FAC
			rcsorAdFacSpTrtoRscRepository.deleteAll(); // RCSOR_AD_FAC_SP_TRTO_RSC
			rcsorAdapFacSpRelRepository.deleteAll(); // RCSOR_ADAP_FAC_SP_REL
			rcsorAdFaPsChVaRepository.deleteAll();// RCSOR_AD_FA_PS_CH_VA
			// Se necesita borrado de tabla paramétrica RCSOP_AD_FAC_VALUE_SPEC
			// antes, y sus relaciones
			// Antes borramos sus relaciones con:
			rcsorAdFaVaPsChVaRepository.deleteAll(); // RCSOR_AD_FA_VA_PS_CH_VA
			rcsorAdapFacVaSusbyRepository.deleteAll();// RCSOR_ADAP_FAC_VA_SUSBY
			rcsorAdFVaSpTrtoGaRepository.deleteAll();// RCSOR_AD_F_VA_SP_TRTO_GA
			rcsorAdFVaSpTrtoRscRepository.deleteAll();// RCSOR_AD_F_VA_SP_TRTO_RSC
			rcsorAdFVaSpTrtoSscRepository.deleteAll();// RCSOR_AD_F_VA_SP_TRTO_SSC
			rcsopAdFacValueSpecRepository.deleteAll();// RCSOP_AD_FAC_VALUE_SPEC
			rcsorAdFaSHResSRepository.deleteAll();// RCSOR_AD_FA_S_H_RES_S
			// Borrar rcsorAdEqRoUseFacSp
			// Antes borramos las relaciones de la tabla de arriba:
			rcsorAdEqRoFSpHChVaRepository.deleteAll(); // RcsorAdEqRoFSpHChVa
			// RCSOR_AD_EQ_RO_F_SP_H_CH_VA
			// --> No encuentro el
			// repositorio
			rcsorAdEqRoFSpHEiRepository.deleteAll();// RcsorAdEqRoFSpHEi
			// RCSOR_AD_EQ_RO_F_SP_H_EI
			// --> No encuentro el
			// repositorio
			rcsorAdEqRoUseFacSpRepository.deleteAll(); // RCSOR_AD_EQ_RO_USE_FAC_SP
			rcsopAdapFacSpecRepository.deleteAll();// RCSOP_ADAP_FAC_SPEC

			// Borramos rcsopAdapEquipRoleSpRepository
			// Borramos primero las relaciones
			rcsorNeLayHEqRoleRepository.deleteAll(); // RCSOR_NE_LAY_H_EQ_ROLE
			rcsorAdEqRoRelIsTriByRepository.deleteAll(); // RCSOR_AD_EQ_RO_REL_IS_TRI_BY
			rcsopAdapEquipRoleSpRepository.deleteAll(); // RCSOP_ADAP_EQUIP_ROLE_SP
			// Borramos RCSOP_ADAP_OPERATION. Eliminamos las relaciones primero:
			// Borramos solo los registros de rcsorAdNetLaySpExe que guardan
			// relación con RcsopAdapOperation
			if (!idsBorrar.isEmpty()) {
				List<RcsorAdNetLaySpExeOp> idsAdNetLaySpExeOp = rcsorAdNetLaySpExeOpRepository
						.getAdNetLaySpExeOpByIdAdapOperation(idsBorrar);
				rcsorAdNetLaySpExeOpRepository.delete(idsAdNetLaySpExeOp); // RCSOR_AD_NET_LAY_SP_EXE_OP
			}

			// Borramos solo los registros de rcsoaAdOpTran que guardan relación
			// con RcsopAdapOperation
			if (!idsBorrar.isEmpty()) {
				List<RcsoaAdOpTran> idsAdOpTran = rcsoaAdOpTranRepository.getAdOpTranByIdAdapOperation(idsBorrar);
				rcsoaAdOpTranRepository.delete(idsAdOpTran); // RCSOA_AD_OP_TRANS
			}

			// Finalmente borramos los registros de adapOperation que no guardan
			// relación con la entidad Adap_Xml
			rcsopAdapOperationRepository.delete(idsAdapOp); // RCSOP_ADAP_OPERATION

			// Borramos RCSOD_ADAP_OPERATION_TYPE
			List<RcsodAdapOperationType> listAdapOpTypeBorrar = new ArrayList<RcsodAdapOperationType>();

			for (RcsodAdapOperationType type : rcsodAdapOperationTypeRepository.findAll()) {
				if (idsAdapTypeBorrar.contains(type.getAoptIdAdapOperationType())
						&& !idsTypeOperationNoBorrar.contains(type.getAoptIdAdapOperationType())
						&& !listAdapOpTypeBorrar.contains(type)) {
					listAdapOpTypeBorrar.add(type);
				}
			}

			rcsodAdapOperationTypeRepository.delete(listAdapOpTypeBorrar);// RCSOD_ADAP_OPERATION_TYPE
			rcsodAdapFaSpecTypeRepository.deleteAll(); // RCSOD_ADAP_FA_SPEC_TYPE
			rcsodAdNetSeTypeSpRepository.deleteAll();// RCSOD_AD_NET_SE_TYPE_SP

			rcsodLockVoiceTypeRepository.deleteAll(); // RCSOD_LOCK_VOICE_TYPE

			rcsopAdapFacSpecRepository.flush();

		} catch (PersistenceException e) {
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}
		LOGGER.info("FIN AdapModelServiceImpl.deleteWholeAdaptationModel");
		return out;
	}

	/**
	 * Servicio de Acceso a Entidad que devuelve el valor de un parámetro de
	 * adaptación
	 * 
	 * @param findadapparametersbyadapcode_in
	 * @param te_Cabecera
	 * @param te_Metadatos
	 * @return
	 * @throws TE_Excepcion
	 */
	public FindAdapParametersByAdapCode_OUT findAdapParametersByAdapCode(
			FindAdapParametersByAdapCode_IN findadapparametersbyadapcode_in, TE_Cabecera te_Cabecera,
			TE_Metadatos te_Metadatos) throws TE_Excepcion {
		FindAdapParametersByAdapCode_OUT result = new FindAdapParametersByAdapCode_OUT();
		LOGGER.info(
				"findAdapParametersByAdapCode - INICIO de la operacion findAdapParametersByAdapCode del servicio AdapModelService");

		List<FindAdapParametersByAdapCodeIdNaDaoOut> adapParameters;
		List<RcsopAdapParameter> adapList;
		AdapParameters_DTO_OUT[] adapParametersList = null;
		try {
			if (findadapparametersbyadapcode_in.getAdapParametersLength() > 0) {
				AdapParameters_DTO_IN[] listaEntrada = findadapparametersbyadapcode_in.getAdapParameters();
				List<String> adapCode = new ArrayList<String>();
				for (int i = 0; i < findadapparametersbyadapcode_in.getAdapParametersLength(); i++) {
					adapCode.add(listaEntrada[i].getAdapCode());

				}

				adapParameters = rcsopAdapParameterRepository.findAdapParametersByAdapCodeIdNa(adapCode);
				adapParametersList = new AdapParameters_DTO_OUT[adapParameters.size()];
				if (adapParameters.size() > 0) {

					int j = 0;
					for (FindAdapParametersByAdapCodeIdNaDaoOut element : adapParameters) {
						adapParametersList[j] = new AdapParameters_DTO_OUT();
						adapParametersList[j].setAdapCode(element.getAdpaIdAdapCode());
						adapParametersList[j].setValue(element.getAdpaNaValue());
						j++;
					}

				}
			} else {
				adapList = rcsopAdapParameterRepository.findAll();
				adapParametersList = new AdapParameters_DTO_OUT[adapList.size()];
				if (adapList.size() > 0) {

					int j = 0;
					for (RcsopAdapParameter element : adapList) {
						adapParametersList[j] = new AdapParameters_DTO_OUT();
						adapParametersList[j].setAdapCode(element.getAdpaIdAdapCode());
						adapParametersList[j].setValue(element.getAdpaNaValue());
						j++;
					}

				}
			}

		} catch (PersistenceException e) {
			LOGGER.error(e.getMessage());
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}
		LOGGER.info(
				"findAdapParametersByAdapCode - Fin de la operacion findAdapParametersByAdapCode del servicio AdapModelService");

		result.setAdapParameters(adapParametersList);
		return result;
	}

	/**
	 * 
	 * @param getadapoperation_in
	 * @param te_Cabecera
	 * @param te_Metadatos
	 * @return
	 * @throws TE_Excepcion
	 */
	public GetAdapOperation_OUT getAdapOperation(GetAdapOperation_IN getadapoperation_in, TE_Cabecera te_Cabecera,
			TE_Metadatos te_Metadatos) throws TE_Excepcion {
		GetAdapOperation_OUT result = new GetAdapOperation_OUT();
		LOGGER.info("getAdapOperation - INICIO de la operacion getAdapOperation del servicio AdapModelService");
		AdapOperation_DTO_IN adaptOp = getadapoperation_in.getAdapOperation();
		ItemWorkflowSpecTask_DTO_IN[] itemWFST = getadapoperation_in.getItemWorkflowSpecTasks();
		List<GetAdapOperationDaoOut> op = null;

		// List<String> listAdapCode = new ArrayList<>();
		// listAdapCode.add(adaptOp.getAdapCode());
		// Boolean isNullable = true;
		try {

			// /**
			// * Se comprueba si LVOT_ID_LOCK_VOICE_TYPE is null para que más
			// tarde no haga JOIN sobre este campo
			// */
			// List<RcsorAdNetLaySpExeOp> listAdNetLaySpExeOp =
			// rcsorAdNetLaySpExeOpRepository.getWholeRcsorAdNetLaySpExeOp(listAdapCode);
			//
			// if(listAdNetLaySpExeOp != null && listAdNetLaySpExeOp.size() >
			// 0){
			//
			// for(RcsorAdNetLaySpExeOp adNetLaySpExeOp : listAdNetLaySpExeOp){
			// if(adNetLaySpExeOp.getRcsodLockVoiceType() != null &&
			// adNetLaySpExeOp.getRcsodLockVoiceType().getLvotIdLockVoiceType()
			// != null){
			// // Si algun campo no es nulo isNullable será false
			// isNullable = false;
			// }
			// }
			// }
			/**
			 * Comprobamos si se envia una lista de ItemWorkflowSpecTasks, en
			 * caso contrario buscamos por codigo
			 */
			if (getadapoperation_in.getItemWorkflowSpecTasksLength() > 0) {
				List<Long> listIdItem = new ArrayList<>();
				for (int i = 0; i < itemWFST.length; i++) {
					listIdItem.add(itemWFST[i].getId());
				}

				// if(isNullable){
				// op =
				// rcsoaAdOpTranRepository.getAdapOperationIdWithoutVoiceType(listIdItem);
				// } else {
				// op = rcsoaAdOpTranRepository.getAdapOperationId(listIdItem);
				// }
				//
				// } else {
				// if(isNullable){
				// op =
				// rcsopAdapOperationRepository.getAdapOperationAdapCodeWithoutVoiceType(adaptOp.getAdapCode());
				// } else {
				// op =
				// rcsopAdapOperationRepository.getAdapOperationAdapCode(adaptOp.getAdapCode());
				// }
			}
		} catch (PersistenceException e) {
			LOGGER.error(e.getMessage());
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}

		/**
		 * Si no se obtienen resultados lanzamos excepcion
		 */
		if (op == null || op.size() == 0) {
			throw new TE_Excepcion(ERROR_000102, DESCRIPTION_000102);
		} else {
			AdapOperation_DTO_OUT[] operations = new AdapOperation_DTO_OUT[op.size()];
			int i = 0;
			/**
			 * Mapeamos la salida
			 */
			for (GetAdapOperationDaoOut operacion : op) {
				AdapOperation_DTO_OUT nuevaOperacion = new AdapOperation_DTO_OUT();

				nuevaOperacion.setId(operacion.getAdopIdAdapOperation());
				nuevaOperacion.setAdapCode(operacion.getAdopIdAdapCode());
				AdapOperationType_DTO_OUT type = new AdapOperationType_DTO_OUT();
				type.setId(operacion.getAoptIdAdapOperationType());
				type.setName(operacion.getAoptNaAdapOperationType());
				nuevaOperacion.setAdapOperationType(type);

				AdapNetLaySpExecutesOp_DTO_OUT[] adapNetLaySpExecutesOp = new AdapNetLaySpExecutesOp_DTO_OUT[1];

				AdapNetLaySpExecutesOp_DTO_OUT adapNet = new AdapNetLaySpExecutesOp_DTO_OUT();

				AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec = new AdapNetworkLayerSpec_DTO_OUT();
				adapNetworkLayerSpec.setAdapCode(operacion.getAnlsIdAdapCode());
				adapNet.setId(operacion.getIdAdapNetLaySpExecutesOp());
				adapNet.setAdapNetworkLayerSpec(adapNetworkLayerSpec);

				LockVoiceType_DTO_OUT lockVoice = new LockVoiceType_DTO_OUT();
				lockVoice.setId(operacion.getLvotIdLockVoiceType());
				lockVoice.setName(operacion.getLvotNaLockVoiceType());
				adapNet.setLockVoiceType(lockVoice);
				adapNet.setPreFulfillment(operacion.getNleoInPrefulfillment());

				adapNetLaySpExecutesOp[0] = adapNet;

				nuevaOperacion.setAdapNetLaySpExecutesOps(adapNetLaySpExecutesOp);
				operations[i] = nuevaOperacion;
				i++;
			}
			result.setAdapOperations(operations);
		}

		LOGGER.info("getAdapOperation - Fin de la operacion getAdapOperation del servicio AdapModelService");
		return result;
	}

	class MapasCache {
		Map<String, RcsopAdNetLayerSp> mapRcsopAdNetLayerSp;
		Map<Long, RcsodAdapOperationType> mapaAdapOperationType;
		Map<Long, RcsopAdapOperation> mapaAdapOperation;
		Map<Long, List<RcsoaAdOpTran>> mapaaAdOpTran;
		Map<String, List<RcsodLockVoiceType>> mapaLockVoiceType;
		Map<String, List<RcsorAdNetLaySpExeOp>> mapaAdNetLaySpExeOp;
		// Bloque 2: obtenAdapServiceLineSpec
		Map<String, RcsopAdapSerLineSp> mapaAdapSerLineSp;
		Map<String, List<RcsorAdapSeLiTrRfsSp>> mapaAdapSeLiTrRfsSp;
		Map<String, List<RcsorAdSeliSpUseNetse>> mapaAdSeliSpUseNetse;
		Map<String, List<RcsorAdSerLiUseAcSp>> mapaAdSerLiUseAcSp;
		Map<Long, List<RcsorAdSlSpUseFaSp>> mapaAdSlSpUseFaSp;
		Map<String, List<RcsorNetLayHasSeLine>> mapaNetLayHasSeLine;
		Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapAccessSpec_DTO_OUT> mapaAdapAccessSpec;
		// Bloque 3: obtenAdapNetwokServiceSpec
		Map<String, RcsopAdapNetSerSp> mapaAdapNetSerSp;
		Map<String, List<RcsodAdNetSeTypeSp>> mapaAdNetSeTypeSp;
		Map<String, List<RcsorAdNetSSpSuby>> mapaAdNetSSpSuby;
		Map<String, List<RcsorAdNSerTrRfsSp>> mapaAdNSerTrRfsSp;
		Map<String, List<RcsorAdNetSSPTby>> mapaAdNetSSpTby;
		Map<String, List<RcsorAdNetSSpTbyRec>> mapaAdNetSSpTbyRec;
		Map<String, List<RcsorAdNSspUseFacSp>> mapaAdNSspUseFacSp;
		Map<String, List<RcsorAdapNetSeSpRel>> mapaAdapNetSeSpRel;
		Map<String, List<RcsorANetSeHEquRoSp>> mapaANetSeHEquRoSp;
		Map<String, List<RcsodAdNetSerSpReTy>> mapaAdNetSerSpReTy;
		Map<String, List<RcsoaAdTaskStepSp>> mapaAdTaskStepSp;
		Map<String, List<RcsorAdNSeReqTrailSp>> mapaAdNSeReqTrailSp;
		Map<String, List<RcsorAdapNssIsTrigBy>> mapaAdapNssIsTrigBy;
		Map<String, List<RcsoaAdTaskStepSp>> mapaRcsorAdNSeSpUseT;
		Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT> mapaAdapNetworkServiceSpecDTO;
		// Bloque 4: obtenAdapAccesSpec
		Map<String, RcsopAdapAccSpec> mapaAdapAccSpec;
		Map<String, List<RcsorAdAcSpURSpChVal>> mapaAdAcSpURSpChVal;
		Map<String, List<RcsorAdAccSpULogRSp>> mapaAdAccSpULogRSp;
		Map<String, List<RcsorAdAccUseEqRoSp>> mapaAdAccUseEqRoSp;
		Map<Long, List<RcsorAdAccUseFacSpec>> mapaAdAccUseFacSpec;
		// Bloque 5: obtenAdapFacilitySpec
		Map<Long, RcsodAdapFaSpecType> mapaAdapFaSpecType;
		Map<String, RcsopAdapFacSpec> mapaAdapFacSpec;
		Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT> mapaAdapFacilitySpecDTOOUT;
		Map<String, List<RcsorAdFaSHResS>> mapaAdFaSHResS;
		Map<String, List<RcsorAdFaPsChVa>> mapaAdFaPsChVa;
		Map<String, List<RcsorAdFacSpTrtoRsc>> mapaAdFacSpTrtoRsc;
		Map<Long, List<RcsorAdFVaSpTrtoGa>> mapaAdFVaSpTrtoGa;
		Map<Long, List<RcsorAdapFacVaSusby>> mapaAdapFacVaSusby;
		Map<Long, List<RcsorAdFaVaPsChVa>> mapaAdFaVaPsChVa;
		Map<Long, List<RcsorAdFVaSpTrtoSsc>> mapaAdFVaSpTrtoSsc;
		Map<Long, List<RcsorAdFVaSpTrtoRsc>> mapaAdFVaSpTrtoRsc;
		Map<Long, List<RcsorAdNssHasFacVal>> mapaAdNssHasFacVal;
		Map<String, List<RcsorAdFacSpTrSeSCv>> mapaAdFacSpTrSeSCv;
		Map<String, List<RcsorAdFacSpTrtoEi>> mapaAdFacSpTrtoEi;
		Map<String, List<RcsorAdapFacSpRel>> mapaAdapFacSpRel;
		Map<String, List<RcsopAdFacValueSpec>> mapaAdFacValueSpec;
		// Bloque 6: obtenAdapTrailSpec
		Map<String, List<RcsopAdapTransportSpec>> mapaAdapTransportSpec;
		Map<String, RcsopAdapTrailSp> mapaAdapTrailSp;
		Map<String, List<RcsopAdapNetTrailSp>> mapaAdapNetTrailSp;
		Map<String, List<RcsorAdNetTrUseFaSp>> mapaAdNetTrUseFaSp;
		Map<String, List<RcsorAdNetTrUseEqRo>> mapaAdNetTrUseEqRo;
		Map<String, List<RcsorAdTrailSpULrSp>> mapaAdTrailSpULrSp;
		Map<String, AdapTrailSpec_DTO_OUT> mapaAdapTrailSpecDTO;
		// Bloque 7: obtenAdapEquipmentRoleSpec
		Map<String, List<RcsopAdapEquipRoleSp>> mapaAdapEquipRoleSp;
		Map<String, List<RcsorAdEqRoRelIsTriBy>> mapaAdEqRoRelIsTriBy;
		Map<Long, List<RcsorAdEqRoUseFacSp>> mapaAdEqRoUseFacSp;
		Map<String, RcsorNeLayHEqRole> mapaNeLayHEqRole;
		Map<String, AdapEquipmentRoleSpec_DTO_OUT> mapaAdapEquipmentRoleSpecDTO;
		// Bloque 8:
		Map<String, RcsopAdapParameter> mapaAdapParameter;
		Map<String, List<RcsorNetLayUseAc>> mapaNetLayUseAc;
		Map<String, List<RcsopAdapFacSpec>> mapaNetLayerHasFac;
		Map<String, List<RcsopAdapTrailSp>> mapaNetLayerHTrail;
		Map<String, List<RcsopAdapNetSerSp>> mapaNeLayerHNetServ;

		MapasCache(ListasFindAll listas) {
			mapRcsopAdNetLayerSp = new HashMap<String, RcsopAdNetLayerSp>();
			for (RcsopAdNetLayerSp elem : listas.getListaRcsopAdNetLayerSp()) {
				String clave = elem.getAnlsIdAdapCode();
				mapRcsopAdNetLayerSp.put(clave, elem);
			}
			// Bloque 1
			mapaAdapOperationType = new HashMap<Long, RcsodAdapOperationType>();
			for (RcsodAdapOperationType elem : listas.getListaAdapOperationType()) {
				Long clave = elem.getAoptIdAdapOperationType();
				mapaAdapOperationType.put(clave, elem);
			}
			mapaAdapOperation = new HashMap<Long, RcsopAdapOperation>();
			for (RcsopAdapOperation elem : listas.getListaAdapOperation()) {
				Long clave = elem.getAdopIdAdapOperation();
				mapaAdapOperation.put(clave, elem);
			}
			mapaaAdOpTran = new HashMap<Long, List<RcsoaAdOpTran>>();
			for (RcsoaAdOpTran elem : listas.getListaaAdOpTran()) {
				Long codigo = elem.getId().getAdopIdAdapOperation();
				List<RcsoaAdOpTran> lista = mapaaAdOpTran.get(codigo);
				if (lista == null) {
					lista = new ArrayList<RcsoaAdOpTran>();
				}
				lista.add(elem);
				mapaaAdOpTran.put(codigo, lista);
			}
			mapaLockVoiceType = new HashMap<String, List<RcsodLockVoiceType>>();
			for (RcsodLockVoiceType elem : listas.getListaLockVoiceType()) {
				// TODO INCLUIR lógica del mapa, puede ser necesaria lista o
				// solo objeto para cada clave
			}
			mapaAdNetLaySpExeOp = new HashMap<String, List<RcsorAdNetLaySpExeOp>>();
			for (RcsorAdNetLaySpExeOp elem : listas.getListaAdNetLaySpExeOp()) {
				String codigo = elem.getRcsopAdNetLayerSp().getAnlsIdAdapCode();
				List<RcsorAdNetLaySpExeOp> lista = mapaAdNetLaySpExeOp.get(codigo);
				if (lista == null) {
					lista = new ArrayList<RcsorAdNetLaySpExeOp>();
				}
				lista.add(elem);
				mapaAdNetLaySpExeOp.put(codigo, lista);
			}

			// Bloque 2: obtenAdapServiceLineSpec
			mapaAdapAccessSpec = new HashMap<String, AdapAccessSpec_DTO_OUT>();
			mapaAdapSerLineSp = new HashMap<String, RcsopAdapSerLineSp>();
			for (RcsopAdapSerLineSp elem : listas.getListaAdapSerLineSp()) {
				String key = elem.getAslsIdAdapCode();
				mapaAdapSerLineSp.put(key, elem);
			}
			mapaAdapSeLiTrRfsSp = new HashMap<String, List<RcsorAdapSeLiTrRfsSp>>();
			for (RcsorAdapSeLiTrRfsSp elem : listas.getListaAdapSeLiTrRfsSp()) {
				String key = elem.getId().getAslsIdAdapCode();
				List<RcsorAdapSeLiTrRfsSp> lista = mapaAdapSeLiTrRfsSp.get(key);
				if (lista == null) {
					lista = new ArrayList<RcsorAdapSeLiTrRfsSp>();
				}
				lista.add(elem);
				mapaAdapSeLiTrRfsSp.put(key, lista);
			}
			mapaAdSeliSpUseNetse = new HashMap<String, List<RcsorAdSeliSpUseNetse>>();

			for (RcsorAdSeliSpUseNetse elem : listas.getListaAdSeliSpUseNetse()) {
				String key = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();

				List<RcsorAdSeliSpUseNetse> lista = mapaAdSeliSpUseNetse.get(key);
				if (lista == null) {
					lista = new ArrayList<RcsorAdSeliSpUseNetse>();
				}
				lista.add(elem);

				mapaAdSeliSpUseNetse.put(key, lista);
			}
			mapaAdSerLiUseAcSp = new HashMap<String, List<RcsorAdSerLiUseAcSp>>();
			for (RcsorAdSerLiUseAcSp elem : listas.getListaAdSerLiUseAcSp()) {
				String key = elem.getRcsopAdapAccSpec().getAacsIdAdapCode();
				List<RcsorAdSerLiUseAcSp> lista = mapaAdSerLiUseAcSp.get(key);
				if (lista == null) {
					lista = new ArrayList<RcsorAdSerLiUseAcSp>();
				}
				lista.add(elem);
				mapaAdSerLiUseAcSp.put(key, lista);
			}
			mapaAdSlSpUseFaSp = new HashMap<Long, List<RcsorAdSlSpUseFaSp>>();
			for (RcsorAdSlSpUseFaSp elem : listas.getListaAdSlSpUseFaSp()) {
				Long key = elem.getRcsopAdapFacSpec().getRcsodAdapFaSpecType().getAfstIdAdapFaSpecType();

				List<RcsorAdSlSpUseFaSp> lista = mapaAdSlSpUseFaSp.get(key);
				if (lista == null) {
					lista = new ArrayList<RcsorAdSlSpUseFaSp>();
				}
				lista.add(elem);
				mapaAdSlSpUseFaSp.put(key, lista);

			}
			mapaNetLayHasSeLine = new HashMap<String, List<RcsorNetLayHasSeLine>>();
			for (RcsorNetLayHasSeLine elem : listas.getListaNetLayHasSeLine()) {
				String key = elem.getId().getAnlsIdAdapCode();
				List<RcsorNetLayHasSeLine> lista = mapaNetLayHasSeLine.get(key);
				if (lista == null) {
					lista = new ArrayList<RcsorNetLayHasSeLine>();
				}
				lista.add(elem);
				mapaNetLayHasSeLine.put(key, lista);

			}

			// Bloque 3: obtenAdapNetworkServiceSpec
			mapaAdapNetSerSp = new HashMap<String, RcsopAdapNetSerSp>();
			for (RcsopAdapNetSerSp elem : listas.getListaAdapNetSerSp()) {
				String clave = elem.getAnssIdAdapCode();
				mapaAdapNetSerSp.put(clave, elem);
			}
			mapaAdNetSeTypeSp = new HashMap<String, List<RcsodAdNetSeTypeSp>>();
			for (RcsodAdNetSeTypeSp elem : listas.getListaAdNetSeTypeSp()) {
				// TODO INCLUIR lógica del mapa, puede ser necesaria lista o
				// solo objeto para cada clave
			}
			mapaAdNetSSpSuby = new HashMap<String, List<RcsorAdNetSSpSuby>>();
			for (RcsorAdNetSSpSuby elem : listas.getListaAdNetSSpSuby()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorAdNetSSpSuby> values = mapaAdNetSSpSuby.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNetSSpSuby>();
					mapaAdNetSSpSuby.put(clave, values);
				}
				values.add(elem);
			}
			mapaAdNSerTrRfsSp = new HashMap<String, List<RcsorAdNSerTrRfsSp>>();
			for (RcsorAdNSerTrRfsSp elem : listas.getListaAdNSerTrRfsSp()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorAdNSerTrRfsSp> values = mapaAdNSerTrRfsSp.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNSerTrRfsSp>();
					mapaAdNSerTrRfsSp.put(clave, values);
				}
				values.add(elem);

			}
			mapaAdNetSSpTby = new HashMap<String, List<RcsorAdNetSSPTby>>();
			for (RcsorAdNetSSPTby elem : listas.getListaAdNetSSpTby()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorAdNetSSPTby> values = mapaAdNetSSpTby.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNetSSPTby>();
					mapaAdNetSSpTby.put(clave, values);
				}
				values.add(elem);
			}
			mapaAdNetSSpTbyRec = new HashMap<String, List<RcsorAdNetSSpTbyRec>>();
			for (RcsorAdNetSSpTbyRec elem : listas.getListaAdNetSSpTbyRec()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorAdNetSSpTbyRec> values = mapaAdNetSSpTbyRec.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNetSSpTbyRec>();
					mapaAdNetSSpTbyRec.put(clave, values);
				}
				values.add(elem);
			}
			mapaAdNSspUseFacSp = new HashMap<String, List<RcsorAdNSspUseFacSp>>();
			for (RcsorAdNSspUseFacSp elem : listas.getListaAdNSspUseFacSp()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorAdNSspUseFacSp> values = mapaAdNSspUseFacSp.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNSspUseFacSp>();
					mapaAdNSspUseFacSp.put(clave, values);
				}
				values.add(elem);
			}

			mapaAdapNetSeSpRel = new HashMap<String, List<RcsorAdapNetSeSpRel>>();
			for (RcsorAdapNetSeSpRel elem : listas.getListaAdapNetSeSpRel()) {
				String clave = elem.getRcsopAdapNetSerSp1().getAnssIdAdapCode();
				List<RcsorAdapNetSeSpRel> values = mapaAdapNetSeSpRel.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdapNetSeSpRel>();
					mapaAdapNetSeSpRel.put(clave, values);
				}
				values.add(elem);
			}

			mapaRcsorAdNSeSpUseT = new HashMap<String, List<RcsoaAdTaskStepSp>>();
			for (RcsorAdNSeSpUseT rcsorAdNSeSpUseT : listas.getListarcsorAdNSeSpUseT()) {
				String clave = rcsorAdNSeSpUseT.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsoaAdTaskStepSp> values = mapaRcsorAdNSeSpUseT.get(clave);
				if (null == values) {
					values = new ArrayList<RcsoaAdTaskStepSp>();
					mapaRcsorAdNSeSpUseT.put(clave, values);
				}
				values.add(rcsorAdNSeSpUseT.getRcsoaAdTaskStepSp());
			}

			mapaANetSeHEquRoSp = new HashMap<String, List<RcsorANetSeHEquRoSp>>();
			for (RcsorANetSeHEquRoSp elem : listas.getListaANetSeHEquRoSp()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorANetSeHEquRoSp> values = mapaANetSeHEquRoSp.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorANetSeHEquRoSp>();
					mapaANetSeHEquRoSp.put(clave, values);
				}
				values.add(elem);
			}

			mapaAdNetSerSpReTy = new HashMap<String, List<RcsodAdNetSerSpReTy>>();
			for (RcsodAdNetSerSpReTy elem : listas.getListaAdNetSerSpReTy()) {
				// TODO INCLUIR lógica del mapa, puede ser necesaria lista o
				// solo objeto para cada clave
			}
			mapaAdTaskStepSp = new HashMap<String, List<RcsoaAdTaskStepSp>>();
			for (RcsoaAdTaskStepSp elem : listas.getListaAdTaskStepSp()) {
				// TODO INCLUIR lógica del mapa, puede ser necesaria lista o
				// solo objeto para cada clave
			}
			mapaAdNSeReqTrailSp = new HashMap<String, List<RcsorAdNSeReqTrailSp>>();
			for (RcsorAdNSeReqTrailSp elem : listas.getListaAdNSeReqTrailSp()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorAdNSeReqTrailSp> values = mapaAdNSeReqTrailSp.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNSeReqTrailSp>();
					mapaAdNSeReqTrailSp.put(clave, values);
				}

				values.add(elem);

			}
			mapaAdapNssIsTrigBy = new HashMap<String, List<RcsorAdapNssIsTrigBy>>();
			for (RcsorAdapNssIsTrigBy elem : listas.getListaAdapNssIsTrigBy()) {
				String clave = elem.getRcsopAdapNetSerSp().getAnssIdAdapCode();
				List<RcsorAdapNssIsTrigBy> values = mapaAdapNssIsTrigBy.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdapNssIsTrigBy>();
					mapaAdapNssIsTrigBy.put(clave, values);
				}
				values.add(elem);
			}

			mapaAdapNetworkServiceSpecDTO = new HashMap<String, AdapNetworkServiceSpec_DTO_OUT>();

			// Bloque 4: obtenAdapAccesSpec

			mapaAdapAccSpec = new HashMap<String, RcsopAdapAccSpec>();

			for (RcsopAdapAccSpec elem : listas.getListaAdapAccSpec()) {

				String key = elem.getAacsIdAdapCode();

				mapaAdapAccSpec.put(key, elem);

			}
			mapaAdAcSpURSpChVal = new HashMap<String, List<RcsorAdAcSpURSpChVal>>();

			for (RcsorAdAcSpURSpChVal elem : listas.getListaAdAcSpURSpChVal()) {

				String key = elem.getId().getAacsIdAdapCode();

				List<RcsorAdAcSpURSpChVal> lista = mapaAdAcSpURSpChVal.get(key);

				if (lista == null) {
					lista = new ArrayList<RcsorAdAcSpURSpChVal>();
				}
				lista.add(elem);
				mapaAdAcSpURSpChVal.put(key, lista);

			}
			mapaAdAccSpULogRSp = new HashMap<String, List<RcsorAdAccSpULogRSp>>();

			for (RcsorAdAccSpULogRSp elem : listas.getListaAdAccSpULogRSp()) {

				String key = elem.getRcsopAdapAccSpec().getAacsIdAdapCode();

				List<RcsorAdAccSpULogRSp> lista = mapaAdAccSpULogRSp.get(key);

				if (lista == null) {
					lista = new ArrayList<RcsorAdAccSpULogRSp>();
				}
				lista.add(elem);
				mapaAdAccSpULogRSp.put(key, lista);

			}
			mapaAdAccUseEqRoSp = new HashMap<String, List<RcsorAdAccUseEqRoSp>>();
			for (RcsorAdAccUseEqRoSp elem : listas.getListaAdAccUseEqRoSp()) {

				String key = elem.getRcsopAdapAccSpec().getAacsIdAdapCode();

				List<RcsorAdAccUseEqRoSp> lista = mapaAdAccUseEqRoSp.get(key);

				if (lista == null) {
					lista = new ArrayList<RcsorAdAccUseEqRoSp>();
				}
				lista.add(elem);
				mapaAdAccUseEqRoSp.put(key, lista);
			}
			mapaAdAccUseFacSpec = new HashMap<Long, List<RcsorAdAccUseFacSpec>>();

			for (RcsorAdAccUseFacSpec elem : listas.getListaAdAccUseFacSpec()) {

				Long key = elem.getAufsIdAdAccUseFacSpec();
				List<RcsorAdAccUseFacSpec> lista = mapaAdAccUseFacSpec.get(key);
				
				if (lista == null) {
					lista = new ArrayList<RcsorAdAccUseFacSpec>();
				}
				lista.add(elem);
				mapaAdAccUseFacSpec.put(key, lista);

			}

			mapaNetLayUseAc = new HashMap<String, List<RcsorNetLayUseAc>>();
			for (RcsorNetLayUseAc elem : listas.getListaNetLayUseAc()) {

				String key = elem.getRcsopAdNetLayerSp().getAnlsIdAdapCode();

				List<RcsorNetLayUseAc> lista = mapaNetLayUseAc.get(key);

				if (lista == null) {
					lista = new ArrayList<RcsorNetLayUseAc>();
				}
				lista.add(elem);
				mapaNetLayUseAc.put(key, lista);

			}
			// Bloque 5: obtenAdapFacilitySpec
			mapaAdapFacilitySpecDTOOUT = new HashMap<String, AdapFacilitySpec_DTO_OUT>();

			mapaAdapFaSpecType = new HashMap<Long, RcsodAdapFaSpecType>();
			for (RcsodAdapFaSpecType elem : listas.getListaAdapFaSpecType()) {
				Long clave = elem.getAfstIdAdapFaSpecType();
				mapaAdapFaSpecType.put(clave, elem);
			}

			mapaAdapFacSpec = new HashMap<String, RcsopAdapFacSpec>();
			for (RcsopAdapFacSpec elem : listas.getListaAdapFacSpec()) {
				String clave = elem.getAfspIdAdapCode();
				mapaAdapFacSpec.put(clave, elem);
			}

			mapaAdFaSHResS = new HashMap<String, List<RcsorAdFaSHResS>>();
			for (RcsorAdFaSHResS elem : listas.getListaAdFaSHResS()) {
				String clave = elem.getId().getAfspIdAdapCode();
				List<RcsorAdFaSHResS> listaRcsorAdFaSHResS = mapaAdFaSHResS.get(clave);
				if (null == listaRcsorAdFaSHResS) {
					listaRcsorAdFaSHResS = new ArrayList<RcsorAdFaSHResS>();
					mapaAdFaSHResS.put(clave, listaRcsorAdFaSHResS);
				}
				listaRcsorAdFaSHResS.add(elem);
			}

			mapaAdFaPsChVa = new HashMap<String, List<RcsorAdFaPsChVa>>();
			for (RcsorAdFaPsChVa elem : listas.getListaAdFaPsChVa()) {
				String clave = elem.getId().getAfspIdAdapCode();
				List<RcsorAdFaPsChVa> listaRcsorAdFaPsChVa = mapaAdFaPsChVa.get(clave);
				if (null == listaRcsorAdFaPsChVa) {
					listaRcsorAdFaPsChVa = new ArrayList<RcsorAdFaPsChVa>();
					mapaAdFaPsChVa.put(clave, listaRcsorAdFaPsChVa);
				}
				listaRcsorAdFaPsChVa.add(elem);
			}

			mapaAdFacSpTrtoRsc = new HashMap<String, List<RcsorAdFacSpTrtoRsc>>();
			for (RcsorAdFacSpTrtoRsc elem : listas.getListaAdFacSpTrtoRsc()) {
				String clave = elem.getId().getAfspIdAdapCode();
				List<RcsorAdFacSpTrtoRsc> listaRcsorAdFacSpTrtoRsc = mapaAdFacSpTrtoRsc.get(clave);
				if (null == listaRcsorAdFacSpTrtoRsc) {
					listaRcsorAdFacSpTrtoRsc = new ArrayList<RcsorAdFacSpTrtoRsc>();
					mapaAdFacSpTrtoRsc.put(clave, listaRcsorAdFacSpTrtoRsc);
				}
				listaRcsorAdFacSpTrtoRsc.add(elem);
			}

			mapaAdFVaSpTrtoGa = new HashMap<Long, List<RcsorAdFVaSpTrtoGa>>();
			for (RcsorAdFVaSpTrtoGa elem : listas.getListaAdFVaSpTrtoGa()) {
				Long clave = elem.getId().getAfvsIdAdFacValueSpec();
				List<RcsorAdFVaSpTrtoGa> listaRcsorAdFVaSpTrtoGa = mapaAdFVaSpTrtoGa.get(clave);
				if (null == listaRcsorAdFVaSpTrtoGa) {
					listaRcsorAdFVaSpTrtoGa = new ArrayList<RcsorAdFVaSpTrtoGa>();
					mapaAdFVaSpTrtoGa.put(clave, listaRcsorAdFVaSpTrtoGa);
				}
				listaRcsorAdFVaSpTrtoGa.add(elem);
			}

			mapaAdapFacVaSusby = new HashMap<Long, List<RcsorAdapFacVaSusby>>();
			for (RcsorAdapFacVaSusby elem : listas.getListaAdapFacVaSusby()) {
				Long clave = elem.getId().getAfvsIdAdFacValueSpec();
				List<RcsorAdapFacVaSusby> listaRcsorAdapFacVaSusby = mapaAdapFacVaSusby.get(clave);
				if (null == listaRcsorAdapFacVaSusby) {
					listaRcsorAdapFacVaSusby = new ArrayList<RcsorAdapFacVaSusby>();
					mapaAdapFacVaSusby.put(clave, listaRcsorAdapFacVaSusby);
				}
				listaRcsorAdapFacVaSusby.add(elem);
			}

			mapaAdFaVaPsChVa = new HashMap<Long, List<RcsorAdFaVaPsChVa>>();
			for (RcsorAdFaVaPsChVa elem : listas.getListaAdFaVaPsChVa()) {
				Long clave = elem.getId().getAfvsIdAdFacValueSpec();
				List<RcsorAdFaVaPsChVa> listaRcsorAdFaVaPsChVa = mapaAdFaVaPsChVa.get(clave);
				if (null == listaRcsorAdFaVaPsChVa) {
					listaRcsorAdFaVaPsChVa = new ArrayList<RcsorAdFaVaPsChVa>();
					mapaAdFaVaPsChVa.put(clave, listaRcsorAdFaVaPsChVa);
				}
				listaRcsorAdFaVaPsChVa.add(elem);
			}

			mapaAdFVaSpTrtoSsc = new HashMap<Long, List<RcsorAdFVaSpTrtoSsc>>();
			for (RcsorAdFVaSpTrtoSsc elem : listas.getListaAdFVaSpTrtoSsc()) {
				Long clave = elem.getId().getAfvsIdAdFacValueSpec();
				List<RcsorAdFVaSpTrtoSsc> listaRcsorAdFVaSpTrtoSsc = mapaAdFVaSpTrtoSsc.get(clave);
				if (null == listaRcsorAdFVaSpTrtoSsc) {
					listaRcsorAdFVaSpTrtoSsc = new ArrayList<RcsorAdFVaSpTrtoSsc>();
					mapaAdFVaSpTrtoSsc.put(clave, listaRcsorAdFVaSpTrtoSsc);
				}
				listaRcsorAdFVaSpTrtoSsc.add(elem);
			}

			mapaAdFVaSpTrtoRsc = new HashMap<Long, List<RcsorAdFVaSpTrtoRsc>>();
			for (RcsorAdFVaSpTrtoRsc elem : listas.getListaAdFVaSpTrtoRsc()) {
				Long clave = elem.getId().getAfvsIdAdFacValueSpec();
				List<RcsorAdFVaSpTrtoRsc> listaRcsorAdFVaSpTrtoRsc = mapaAdFVaSpTrtoRsc.get(clave);
				if (null == listaRcsorAdFVaSpTrtoRsc) {
					listaRcsorAdFVaSpTrtoRsc = new ArrayList<RcsorAdFVaSpTrtoRsc>();
					mapaAdFVaSpTrtoRsc.put(clave, listaRcsorAdFVaSpTrtoRsc);
				}
				listaRcsorAdFVaSpTrtoRsc.add(elem);
			}

			mapaAdNssHasFacVal = new HashMap<Long, List<RcsorAdNssHasFacVal>>();
			for (RcsorAdNssHasFacVal elem : listas.getListaAdNssHasFacVal()) {
				Long clave = elem.getId().getAfvsIdAdFacValueSpec();
				List<RcsorAdNssHasFacVal> listaRcsorAdNssHasFacVal = mapaAdNssHasFacVal.get(clave);
				if (null == listaRcsorAdNssHasFacVal) {
					listaRcsorAdNssHasFacVal = new ArrayList<RcsorAdNssHasFacVal>();
					mapaAdNssHasFacVal.put(clave, listaRcsorAdNssHasFacVal);
				}
				listaRcsorAdNssHasFacVal.add(elem);
			}

			mapaAdFacSpTrSeSCv = new HashMap<String, List<RcsorAdFacSpTrSeSCv>>();
			for (RcsorAdFacSpTrSeSCv elem : listas.getListaAdFacSpTrSeSCv()) {
				String clave = elem.getId().getAfspIdAdapCode();
				List<RcsorAdFacSpTrSeSCv> listaRcsorAdFacSpTrSeSCv = mapaAdFacSpTrSeSCv.get(clave);
				if (null == listaRcsorAdFacSpTrSeSCv) {
					listaRcsorAdFacSpTrSeSCv = new ArrayList<RcsorAdFacSpTrSeSCv>();
					mapaAdFacSpTrSeSCv.put(clave, listaRcsorAdFacSpTrSeSCv);
				}
				listaRcsorAdFacSpTrSeSCv.add(elem);
			}

			mapaAdFacSpTrtoEi = new HashMap<String, List<RcsorAdFacSpTrtoEi>>();
			for (RcsorAdFacSpTrtoEi elem : listas.getListaAdFacSpTrtoEi()) {
				String clave = elem.getId().getAfspIdAdapCode();
				List<RcsorAdFacSpTrtoEi> listaRcsorAdFacSpTrtoEi = mapaAdFacSpTrtoEi.get(clave);
				if (null == listaRcsorAdFacSpTrtoEi) {
					listaRcsorAdFacSpTrtoEi = new ArrayList<RcsorAdFacSpTrtoEi>();
					mapaAdFacSpTrtoEi.put(clave, listaRcsorAdFacSpTrtoEi);
				}
				listaRcsorAdFacSpTrtoEi.add(elem);
			}

			mapaAdapFacSpRel = new HashMap<String, List<RcsorAdapFacSpRel>>();
			for (RcsorAdapFacSpRel elem : listas.getListaAdapFacSpRel()) {
				String clave = elem.getRcsopAdapFacSpec1().getAfspIdAdapCode();
				List<RcsorAdapFacSpRel> listaRcsorAdapFacSpRel = mapaAdapFacSpRel.get(clave);
				if (null == listaRcsorAdapFacSpRel) {
					listaRcsorAdapFacSpRel = new ArrayList<RcsorAdapFacSpRel>();
					mapaAdapFacSpRel.put(clave, listaRcsorAdapFacSpRel);
				}
				listaRcsorAdapFacSpRel.add(elem);
			}

			mapaAdFacValueSpec = new HashMap<String, List<RcsopAdFacValueSpec>>();
			for (RcsopAdFacValueSpec elem : listas.getListaAdFacValueSpec()) {
				String clave = elem.getRcsopAdapFacSpec().getAfspIdAdapCode();
				List<RcsopAdFacValueSpec> listaRcsopAdFacValueSpec = mapaAdFacValueSpec.get(clave);
				if (null == listaRcsopAdFacValueSpec) {
					listaRcsopAdFacValueSpec = new ArrayList<RcsopAdFacValueSpec>();
					mapaAdFacValueSpec.put(clave, listaRcsopAdFacValueSpec);
				}
				listaRcsopAdFacValueSpec.add(elem);
			}

			mapaNetLayerHasFac = new HashMap<String, List<RcsopAdapFacSpec>>();
			for (RcsorNetLayerHasFac elem : listas.getListaNetLayerHasFac()) {
				String clave = elem.getId().getAnlsIdAdapCode();
				List<RcsopAdapFacSpec> listaRcsopAdapFacSpec = mapaNetLayerHasFac.get(clave);
				if (null == listaRcsopAdapFacSpec) {
					listaRcsopAdapFacSpec = new ArrayList<RcsopAdapFacSpec>();
					mapaNetLayerHasFac.put(clave, listaRcsopAdapFacSpec);
				}
				listaRcsopAdapFacSpec.add(elem.getRcsopAdapFacSpec());
			}
			// Bloque 6: obtenAdapTrailSpec
			mapaAdapTrailSpecDTO = new HashMap<String, AdapTrailSpec_DTO_OUT>();

			mapaAdapTransportSpec = new HashMap<String, List<RcsopAdapTransportSpec>>();
			for (RcsopAdapTransportSpec elem : listas.getListaAdapTransportSpec()) {
				// TODO INCLUIR lógica del mapa, puede ser necesaria lista o
				// solo objeto para cada clave
			}
			mapaAdapTrailSp = new HashMap<String, RcsopAdapTrailSp>();
			for (RcsopAdapTrailSp elem : listas.getListaAdapTrailSp()) {
				String clave = elem.getAtrrIdAdapCode();
				mapaAdapTrailSp.put(clave, elem);
			}
			mapaAdapNetTrailSp = new HashMap<String, List<RcsopAdapNetTrailSp>>();
			for (RcsopAdapNetTrailSp elem : listas.getListaAdapNetTrailSp()) {
				String clave = elem.getRcsopAdapTrailSp().getAtrrIdAdapCode();
				List<RcsopAdapNetTrailSp> values = mapaAdapNetTrailSp.get(clave);
				if (null == values) {
					values = new ArrayList<RcsopAdapNetTrailSp>();
					mapaAdapNetTrailSp.put(clave, values);
				}
				values.add(elem);
			}
			mapaAdNetTrUseFaSp = new HashMap<String, List<RcsorAdNetTrUseFaSp>>();
			for (RcsorAdNetTrUseFaSp elem : listas.getListaAdNetTrUseFaSp()) {
				String clave = elem.getRcsopAdapNetTrailSp().getAntsIdAdapCode();
				List<RcsorAdNetTrUseFaSp> values = mapaAdNetTrUseFaSp.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNetTrUseFaSp>();
					mapaAdNetTrUseFaSp.put(clave, values);
				}
				values.add(elem);
			}
			mapaAdNetTrUseEqRo = new HashMap<String, List<RcsorAdNetTrUseEqRo>>();
			for (RcsorAdNetTrUseEqRo elem : listas.getListaAdNetTrUseEqRo()) {
				String clave = elem.getRcsopAdapNetTrailSp().getAntsIdAdapCode();
				List<RcsorAdNetTrUseEqRo> values = mapaAdNetTrUseEqRo.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdNetTrUseEqRo>();
					mapaAdNetTrUseEqRo.put(clave, values);
				}
				values.add(elem);
			}
			mapaAdTrailSpULrSp = new HashMap<String, List<RcsorAdTrailSpULrSp>>();
			for (RcsorAdTrailSpULrSp elem : listas.getListaAdTrailSpULrSp()) {
				String clave = elem.getRcsopAdapTrailSp().getAtrrIdAdapCode();
				List<RcsorAdTrailSpULrSp> values = mapaAdTrailSpULrSp.get(clave);
				if (null == values) {
					values = new ArrayList<RcsorAdTrailSpULrSp>();
					mapaAdTrailSpULrSp.put(clave, values);
				}
				values.add(elem);
			}

			// Bloque 7: obtenAdapEquipmentRoleSpec
			mapaAdapEquipmentRoleSpecDTO = new HashMap<String, AdapEquipmentRoleSpec_DTO_OUT>();
			mapaAdapEquipRoleSp = new HashMap<String, List<RcsopAdapEquipRoleSp>>();
			for (RcsorAdNetTrUseEqRo value : listas.getListaAdNetTrUseEqRo()) {
				String key = value.getRcsopAdapEquipRoleSp().getAersIdAdapCode();
				List<RcsopAdapEquipRoleSp> listRcsopAdapEquipRoleSp;
				
				if (!mapaAdapEquipRoleSp.containsKey(key)) {
					listRcsopAdapEquipRoleSp = new ArrayList<RcsopAdapEquipRoleSp>();
				} else {
					listRcsopAdapEquipRoleSp = mapaAdapEquipRoleSp.get(key);
				}
				listRcsopAdapEquipRoleSp.add(value.getRcsopAdapEquipRoleSp());
				
				mapaAdapEquipRoleSp.put(key, listRcsopAdapEquipRoleSp);
			}

			mapaAdEqRoRelIsTriBy = new HashMap<String, List<RcsorAdEqRoRelIsTriBy>>();
			
			for (RcsorAdEqRoRelIsTriBy value : listas.getListaAdEqRoRelIsTriBy()) {
				List<RcsorAdEqRoRelIsTriBy> listRcsorAdEqRoRelIsTriBy;
				String key = value.getId().getAersIdAdapCode();

				if (!mapaAdEqRoRelIsTriBy.containsKey(key)) {
					listRcsorAdEqRoRelIsTriBy = new ArrayList<RcsorAdEqRoRelIsTriBy>();
				} else {
					listRcsorAdEqRoRelIsTriBy = mapaAdEqRoRelIsTriBy.get(key);
				}

				listRcsorAdEqRoRelIsTriBy.add(value);
				mapaAdEqRoRelIsTriBy.put(key, listRcsorAdEqRoRelIsTriBy);

			}
			
			mapaAdEqRoUseFacSp = new HashMap<Long, List<RcsorAdEqRoUseFacSp>>();
			for (RcsorAdEqRoUseFacSp value : listas.getListaAdEqRoUseFacSp()) {
				Long key = value.getErufIdAdEqRoUseFacSp();

				List<RcsorAdEqRoUseFacSp> listaRcsorAdEqRoUseFacSp;
				if (!mapaAdEqRoRelIsTriBy.containsKey(key)) {
					listaRcsorAdEqRoUseFacSp = new ArrayList<RcsorAdEqRoUseFacSp>();
				} else {
					listaRcsorAdEqRoUseFacSp = mapaAdEqRoUseFacSp.get(key);
				}
				listaRcsorAdEqRoUseFacSp.add(value);
				mapaAdEqRoUseFacSp.put(key, listaRcsorAdEqRoUseFacSp);
			}

			mapaNeLayHEqRole = new HashMap<String, RcsorNeLayHEqRole>();
			for (RcsorNeLayHEqRole value : listas.getListaNeLayHEqRole()) {
				String key = value.getRcsopAdNetLayerSp().getAnlsIdAdapCode();
				mapaNeLayHEqRole.put(key, value);
			}
			// Bloque 8:
			mapaAdapParameter = new HashMap<String, RcsopAdapParameter>();
			for (RcsopAdapParameter elem : listas.getListaAdapParameter()) {
				String clave = elem.getAdpaIdAdapCode();
				mapaAdapParameter.put(clave, elem);
			}

			mapaNetLayUseAc = new HashMap<String, List<RcsorNetLayUseAc>>();
			for (RcsorNetLayUseAc elem : listas.getListaNetLayUseAc()) {
				// TODO INCLUIR lógica del mapa, puede ser necesaria lista o
				// solo objeto para cada clave
			}

			mapaNetLayerHTrail = new HashMap<String, List<RcsopAdapTrailSp>>();

			for (RcsorNetLayerHTrail elem : listas.getListaNetLayerHTrail()) {
				String Clave = elem.getRcsopAdNetLayerSp().getAnlsIdAdapCode();
				List<RcsopAdapTrailSp> values = mapaNetLayerHTrail.get(Clave);
				if (null == values) {
					values = new ArrayList<RcsopAdapTrailSp>();
					mapaNetLayerHTrail.put(Clave, values);
				}
				values.add(elem.getRcsopAdapTrailSp());
			}

			mapaNeLayerHNetServ = new HashMap<String, List<RcsopAdapNetSerSp>>();
			for (RcsorNeLayerHNetServ elem : listas.getListaNeLayerHNetServ()) {
				String clave = elem.getRcsopAdNetLayerSp().getAnlsIdAdapCode();
				List<RcsopAdapNetSerSp> values = mapaNeLayerHNetServ.get(clave);
				if (null == values) {
					values = new ArrayList<RcsopAdapNetSerSp>();
					mapaNeLayerHNetServ.put(clave, values);
				}
				values.add(elem.getRcsopAdapNetSerSp());
			}

			// Mapas Adicionales.
			mapaAdapFacilitySpecDTOOUT = new HashMap<String, AdapFacilitySpec_DTO_OUT>();
			mapaAdapNetworkServiceSpecDTO = new HashMap<String, AdapNetworkServiceSpec_DTO_OUT>();
		}

		/**
		 * @return the mapRcsopAdNetLayerSp
		 */
		public Map<String, RcsopAdNetLayerSp> getMapRcsopAdNetLayerSp() {
			return mapRcsopAdNetLayerSp;
		}

		/**
		 * @param mapRcsopAdNetLayerSp
		 *            the mapRcsopAdNetLayerSp to set
		 */
		public void setMapRcsopAdNetLayerSp(Map<String, RcsopAdNetLayerSp> mapRcsopAdNetLayerSp) {
			this.mapRcsopAdNetLayerSp = mapRcsopAdNetLayerSp;
		}

		/**
		 * @return the mapaAdapOperationType
		 */
		public Map<Long, RcsodAdapOperationType> getMapaAdapOperationType() {
			return mapaAdapOperationType;
		}

		/**
		 * @param mapaAdapOperationType
		 *            the mapaAdapOperationType to set
		 */
		public void setMapaAdapOperationType(Map<Long, RcsodAdapOperationType> mapaAdapOperationType) {
			this.mapaAdapOperationType = mapaAdapOperationType;
		}

		/**
		 * @return the mapaAdapOperation
		 */
		public Map<Long, RcsopAdapOperation> getMapaAdapOperation() {
			return mapaAdapOperation;
		}

		/**
		 * @param mapaAdapOperation
		 *            the mapaAdapOperation to set
		 */
		public void setMapaAdapOperation(Map<Long, RcsopAdapOperation> mapaAdapOperation) {
			this.mapaAdapOperation = mapaAdapOperation;
		}

		/**
		 * @return the mapaaAdOpTran
		 */
		public Map<Long, List<RcsoaAdOpTran>> getMapaaAdOpTran() {
			return mapaaAdOpTran;
		}

		/**
		 * @param mapaaAdOpTran
		 *            the mapaaAdOpTran to set
		 */
		public void setMapaaAdOpTran(Map<Long, List<RcsoaAdOpTran>> mapaaAdOpTran) {
			this.mapaaAdOpTran = mapaaAdOpTran;
		}

		/**
		 * @return the mapaLockVoiceType
		 */
		public Map<String, List<RcsodLockVoiceType>> getMapaLockVoiceType() {
			return mapaLockVoiceType;
		}

		/**
		 * @param mapaLockVoiceType
		 *            the mapaLockVoiceType to set
		 */
		public void setMapaLockVoiceType(Map<String, List<RcsodLockVoiceType>> mapaLockVoiceType) {
			this.mapaLockVoiceType = mapaLockVoiceType;
		}

		/**
		 * @return the mapaAdNetLaySpExeOp
		 */
		public Map<String, List<RcsorAdNetLaySpExeOp>> getMapaAdNetLaySpExeOp() {
			return mapaAdNetLaySpExeOp;
		}

		/**
		 * @param mapaAdNetLaySpExeOp
		 *            the mapaAdNetLaySpExeOp to set
		 */
		public void setMapaAdNetLaySpExeOp(Map<String, List<RcsorAdNetLaySpExeOp>> mapaAdNetLaySpExeOp) {
			this.mapaAdNetLaySpExeOp = mapaAdNetLaySpExeOp;
		}

		/**
		 * @return the mapaAdapSerLineSp
		 */
		public Map<String, RcsopAdapSerLineSp> getMapaAdapSerLineSp() {
			return mapaAdapSerLineSp;
		}

		/**
		 * @param mapaAdapSerLineSp
		 *            the mapaAdapSerLineSp to set
		 */
		public void setMapaAdapSerLineSp(Map<String, RcsopAdapSerLineSp> mapaAdapSerLineSp) {
			this.mapaAdapSerLineSp = mapaAdapSerLineSp;
		}

		/**
		 * @return the mapaAdapSeLiTrRfsSp
		 */
		public Map<String, List<RcsorAdapSeLiTrRfsSp>> getMapaAdapSeLiTrRfsSp() {
			return mapaAdapSeLiTrRfsSp;
		}

		/**
		 * @param mapaAdapSeLiTrRfsSp
		 *            the mapaAdapSeLiTrRfsSp to set
		 */
		public void setMapaAdapSeLiTrRfsSp(Map<String, List<RcsorAdapSeLiTrRfsSp>> mapaAdapSeLiTrRfsSp) {
			this.mapaAdapSeLiTrRfsSp = mapaAdapSeLiTrRfsSp;
		}

		/**
		 * @return the mapaAdSeliSpUseNetse
		 */
		public Map<String, List<RcsorAdSeliSpUseNetse>> getMapaAdSeliSpUseNetse() {
			return mapaAdSeliSpUseNetse;
		}

		/**
		 * @param mapaAdSeliSpUseNetse
		 *            the mapaAdSeliSpUseNetse to set
		 */
		public void setMapaAdSeliSpUseNetse(Map<String, List<RcsorAdSeliSpUseNetse>> mapaAdSeliSpUseNetse) {
			this.mapaAdSeliSpUseNetse = mapaAdSeliSpUseNetse;
		}

		/**
		 * @return the mapaAdSerLiUseAcSp
		 */
		public Map<String, List<RcsorAdSerLiUseAcSp>> getMapaAdSerLiUseAcSp() {
			return mapaAdSerLiUseAcSp;
		}

		/**
		 * @param mapaAdSerLiUseAcSp
		 *            the mapaAdSerLiUseAcSp to set
		 */
		public void setMapaAdSerLiUseAcSp(Map<String, List<RcsorAdSerLiUseAcSp>> mapaAdSerLiUseAcSp) {
			this.mapaAdSerLiUseAcSp = mapaAdSerLiUseAcSp;
		}

		/**
		 * @return the mapaAdSlSpUseFaSp
		 */
		public Map<Long, List<RcsorAdSlSpUseFaSp>> getMapaAdSlSpUseFaSp() {
			return mapaAdSlSpUseFaSp;
		}

		/**
		 * @param mapaAdSlSpUseFaSp
		 *            the mapaAdSlSpUseFaSp to set
		 */
		public void setMapaAdSlSpUseFaSp(Map<Long, List<RcsorAdSlSpUseFaSp>> mapaAdSlSpUseFaSp) {
			this.mapaAdSlSpUseFaSp = mapaAdSlSpUseFaSp;
		}

		/**
		 * @return the mapaNetLayHasSeLine
		 */
		public Map<String, List<RcsorNetLayHasSeLine>> getMapaNetLayHasSeLine() {
			return mapaNetLayHasSeLine;
		}

		/**
		 * @param mapaNetLayHasSeLine
		 *            the mapaNetLayHasSeLine to set
		 */
		public void setMapaNetLayHasSeLine(Map<String, List<RcsorNetLayHasSeLine>> mapaNetLayHasSeLine) {
			this.mapaNetLayHasSeLine = mapaNetLayHasSeLine;
		}

		/**
		 * @return the mapaAdapNetSerSp
		 */
		public Map<String, RcsopAdapNetSerSp> getMapaAdapNetSerSp() {
			return mapaAdapNetSerSp;
		}

		/**
		 * @param mapaAdapNetSerSp
		 *            the mapaAdapNetSerSp to set
		 */
		public void setMapaAdapNetSerSp(Map<String, RcsopAdapNetSerSp> mapaAdapNetSerSp) {
			this.mapaAdapNetSerSp = mapaAdapNetSerSp;
		}

		/**
		 * @return the mapaAdNetSeTypeSp
		 */
		public Map<String, List<RcsodAdNetSeTypeSp>> getMapaAdNetSeTypeSp() {
			return mapaAdNetSeTypeSp;
		}

		/**
		 * @param mapaAdNetSeTypeSp
		 *            the mapaAdNetSeTypeSp to set
		 */
		public void setMapaAdNetSeTypeSp(Map<String, List<RcsodAdNetSeTypeSp>> mapaAdNetSeTypeSp) {
			this.mapaAdNetSeTypeSp = mapaAdNetSeTypeSp;
		}

		/**
		 * @return the mapaAdNetSSpSuby
		 */
		public Map<String, List<RcsorAdNetSSpSuby>> getMapaAdNetSSpSuby() {
			return mapaAdNetSSpSuby;
		}

		/**
		 * @param mapaAdNetSSpSuby
		 *            the mapaAdNetSSpSuby to set
		 */
		public void setMapaAdNetSSpSuby(Map<String, List<RcsorAdNetSSpSuby>> mapaAdNetSSpSuby) {
			this.mapaAdNetSSpSuby = mapaAdNetSSpSuby;
		}

		/**
		 * @return the mapaAdNSerTrRfsSp
		 */
		public Map<String, List<RcsorAdNSerTrRfsSp>> getMapaAdNSerTrRfsSp() {
			return mapaAdNSerTrRfsSp;
		}

		/**
		 * @param mapaAdNSerTrRfsSp
		 *            the mapaAdNSerTrRfsSp to set
		 */
		public void setMapaAdNSerTrRfsSp(Map<String, List<RcsorAdNSerTrRfsSp>> mapaAdNSerTrRfsSp) {
			this.mapaAdNSerTrRfsSp = mapaAdNSerTrRfsSp;
		}

		/**
		 * @return the mapaAdNetSSpTby
		 */
		public Map<String, List<RcsorAdNetSSPTby>> getMapaAdNetSSpTby() {
			return mapaAdNetSSpTby;
		}

		/**
		 * @param mapaAdNetSSpTby
		 *            the mapaAdNetSSpTby to set
		 */
		public void setMapaAdNetSSpTby(Map<String, List<RcsorAdNetSSPTby>> mapaAdNetSSpTby) {
			this.mapaAdNetSSpTby = mapaAdNetSSpTby;
		}

		/**
		 * @return the mapaAdNetSSpTbyRec
		 */
		public Map<String, List<RcsorAdNetSSpTbyRec>> getMapaAdNetSSpTbyRec() {
			return mapaAdNetSSpTbyRec;
		}

		/**
		 * @param mapaAdNetSSpTbyRec
		 *            the mapaAdNetSSpTbyRec to set
		 */
		public void setMapaAdNetSSpTbyRec(Map<String, List<RcsorAdNetSSpTbyRec>> mapaAdNetSSpTbyRec) {
			this.mapaAdNetSSpTbyRec = mapaAdNetSSpTbyRec;
		}

		/**
		 * @return the mapaAdNSspUseFacSp
		 */
		public Map<String, List<RcsorAdNSspUseFacSp>> getMapaAdNSspUseFacSp() {
			return mapaAdNSspUseFacSp;
		}

		/**
		 * @param mapaAdNSspUseFacSp
		 *            the mapaAdNSspUseFacSp to set
		 */
		public void setMapaAdNSspUseFacSp(Map<String, List<RcsorAdNSspUseFacSp>> mapaAdNSspUseFacSp) {
			this.mapaAdNSspUseFacSp = mapaAdNSspUseFacSp;
		}

		/**
		 * @return the mapaAdapNetSeSpRel
		 */
		public Map<String, List<RcsorAdapNetSeSpRel>> getMapaAdapNetSeSpRel() {
			return mapaAdapNetSeSpRel;
		}

		/**
		 * @param mapaAdapNetSeSpRel
		 *            the mapaAdapNetSeSpRel to set
		 */
		public void setMapaAdapNetSeSpRel(Map<String, List<RcsorAdapNetSeSpRel>> mapaAdapNetSeSpRel) {
			this.mapaAdapNetSeSpRel = mapaAdapNetSeSpRel;
		}

		/**
		 * @return the mapaANetSeHEquRoSp
		 */
		public Map<String, List<RcsorANetSeHEquRoSp>> getMapaANetSeHEquRoSp() {
			return mapaANetSeHEquRoSp;
		}

		/**
		 * @param mapaANetSeHEquRoSp
		 *            the mapaANetSeHEquRoSp to set
		 */
		public void setMapaANetSeHEquRoSp(Map<String, List<RcsorANetSeHEquRoSp>> mapaANetSeHEquRoSp) {
			this.mapaANetSeHEquRoSp = mapaANetSeHEquRoSp;
		}

		/**
		 * @return the mapaAdNetSerSpReTy
		 */
		public Map<String, List<RcsodAdNetSerSpReTy>> getMapaAdNetSerSpReTy() {
			return mapaAdNetSerSpReTy;
		}

		/**
		 * @param mapaAdNetSerSpReTy
		 *            the mapaAdNetSerSpReTy to set
		 */
		public void setMapaAdNetSerSpReTy(Map<String, List<RcsodAdNetSerSpReTy>> mapaAdNetSerSpReTy) {
			this.mapaAdNetSerSpReTy = mapaAdNetSerSpReTy;
		}

		/**
		 * @return the mapaAdTaskStepSp
		 */
		public Map<String, List<RcsoaAdTaskStepSp>> getMapaAdTaskStepSp() {
			return mapaAdTaskStepSp;
		}

		/**
		 * @param mapaAdTaskStepSp
		 *            the mapaAdTaskStepSp to set
		 */
		public void setMapaAdTaskStepSp(Map<String, List<RcsoaAdTaskStepSp>> mapaAdTaskStepSp) {
			this.mapaAdTaskStepSp = mapaAdTaskStepSp;
		}

		/**
		 * @return the mapaAdNSeReqTrailSp
		 */
		public Map<String, List<RcsorAdNSeReqTrailSp>> getMapaAdNSeReqTrailSp() {
			return mapaAdNSeReqTrailSp;
		}

		/**
		 * @param mapaAdNSeReqTrailSp
		 *            the mapaAdNSeReqTrailSp to set
		 */
		public void setMapaAdNSeReqTrailSp(Map<String, List<RcsorAdNSeReqTrailSp>> mapaAdNSeReqTrailSp) {
			this.mapaAdNSeReqTrailSp = mapaAdNSeReqTrailSp;
		}

		/**
		 * @return the mapaAdapNssIsTrigBy
		 */
		public Map<String, List<RcsorAdapNssIsTrigBy>> getMapaAdapNssIsTrigBy() {
			return mapaAdapNssIsTrigBy;
		}

		/**
		 * @param mapaAdapNssIsTrigBy
		 *            the mapaAdapNssIsTrigBy to set
		 */
		public void setMapaAdapNssIsTrigBy(Map<String, List<RcsorAdapNssIsTrigBy>> mapaAdapNssIsTrigBy) {
			this.mapaAdapNssIsTrigBy = mapaAdapNssIsTrigBy;
		}

		/**
		 * @return the mapaAdapAccSpec
		 */
		public Map<String, RcsopAdapAccSpec> getMapaAdapAccSpec() {
			return mapaAdapAccSpec;
		}

		/**
		 * @param mapaAdapAccSpec
		 *            the mapaAdapAccSpec to set
		 */
		public void setMapaAdapAccSpec(Map<String, RcsopAdapAccSpec> mapaAdapAccSpec) {
			this.mapaAdapAccSpec = mapaAdapAccSpec;
		}

		/**
		 * @return the mapaAdAcSpURSpChVal
		 */
		public Map<String, List<RcsorAdAcSpURSpChVal>> getMapaAdAcSpURSpChVal() {
			return mapaAdAcSpURSpChVal;
		}

		/**
		 * @param mapaAdAcSpURSpChVal
		 *            the mapaAdAcSpURSpChVal to set
		 */
		public void setMapaAdAcSpURSpChVal(Map<String, List<RcsorAdAcSpURSpChVal>> mapaAdAcSpURSpChVal) {
			this.mapaAdAcSpURSpChVal = mapaAdAcSpURSpChVal;
		}

		/**
		 * @return the mapaAdAccSpULogRSp
		 */
		public Map<String, List<RcsorAdAccSpULogRSp>> getMapaAdAccSpULogRSp() {
			return mapaAdAccSpULogRSp;
		}

		/**
		 * @param mapaAdAccSpULogRSp
		 *            the mapaAdAccSpULogRSp to set
		 */
		public void setMapaAdAccSpULogRSp(Map<String, List<RcsorAdAccSpULogRSp>> mapaAdAccSpULogRSp) {
			this.mapaAdAccSpULogRSp = mapaAdAccSpULogRSp;
		}

		/**
		 * @return the mapaAdAccUseEqRoSp
		 */
		public Map<String, List<RcsorAdAccUseEqRoSp>> getMapaAdAccUseEqRoSp() {
			return mapaAdAccUseEqRoSp;
		}

		/**
		 * @param mapaAdAccUseEqRoSp
		 *            the mapaAdAccUseEqRoSp to set
		 */
		public void setMapaAdAccUseEqRoSp(Map<String, List<RcsorAdAccUseEqRoSp>> mapaAdAccUseEqRoSp) {
			this.mapaAdAccUseEqRoSp = mapaAdAccUseEqRoSp;
		}

		/**
		 * @return the mapaAdAccUseFacSpec
		 */
		public Map<Long, List<RcsorAdAccUseFacSpec>> getMapaAdAccUseFacSpec() {
			return mapaAdAccUseFacSpec;
		}

		/**
		 * @param mapaAdAccUseFacSpec
		 *            the mapaAdAccUseFacSpec to set
		 */
		public void setMapaAdAccUseFacSpec(Map<Long, List<RcsorAdAccUseFacSpec>> mapaAdAccUseFacSpec) {
			this.mapaAdAccUseFacSpec = mapaAdAccUseFacSpec;
		}

		/**
		 * @return the mapaAdapFaSpecType
		 */
		public Map<Long, RcsodAdapFaSpecType> getMapaAdapFaSpecType() {
			return mapaAdapFaSpecType;
		}

		/**
		 * @param mapaAdapFaSpecType
		 *            the mapaAdapFaSpecType to set
		 */
		public void setMapaAdapFaSpecType(Map<Long, RcsodAdapFaSpecType> mapaAdapFaSpecType) {
			this.mapaAdapFaSpecType = mapaAdapFaSpecType;
		}

		/**
		 * @return the mapaAdapFacSpec
		 */
		public Map<String, RcsopAdapFacSpec> getMapaAdapFacSpec() {
			return mapaAdapFacSpec;
		}

		/**
		 * @param mapaAdapFacSpec
		 *            the mapaAdapFacSpec to set
		 */
		public void setMapaAdapFacSpec(Map<String, RcsopAdapFacSpec> mapaAdapFacSpec) {
			this.mapaAdapFacSpec = mapaAdapFacSpec;
		}

		/**
		 * @return the mapaAdFaSHResS
		 */
		public Map<String, List<RcsorAdFaSHResS>> getMapaAdFaSHResS() {
			return mapaAdFaSHResS;
		}

		/**
		 * @param mapaAdFaSHResS
		 *            the mapaAdFaSHResS to set
		 */
		public void setMapaAdFaSHResS(Map<String, List<RcsorAdFaSHResS>> mapaAdFaSHResS) {
			this.mapaAdFaSHResS = mapaAdFaSHResS;
		}

		/**
		 * @return the mapaAdFaPsChVa
		 */
		public Map<String, List<RcsorAdFaPsChVa>> getMapaAdFaPsChVa() {
			return mapaAdFaPsChVa;
		}

		/**
		 * @param mapaAdFaPsChVa
		 *            the mapaAdFaPsChVa to set
		 */
		public void setMapaAdFaPsChVa(Map<String, List<RcsorAdFaPsChVa>> mapaAdFaPsChVa) {
			this.mapaAdFaPsChVa = mapaAdFaPsChVa;
		}

		/**
		 * @return the mapaAdFacSpTrtoRsc
		 */
		public Map<String, List<RcsorAdFacSpTrtoRsc>> getMapaAdFacSpTrtoRsc() {
			return mapaAdFacSpTrtoRsc;
		}

		/**
		 * @param mapaAdFacSpTrtoRsc
		 *            the mapaAdFacSpTrtoRsc to set
		 */
		public void setMapaAdFacSpTrtoRsc(Map<String, List<RcsorAdFacSpTrtoRsc>> mapaAdFacSpTrtoRsc) {
			this.mapaAdFacSpTrtoRsc = mapaAdFacSpTrtoRsc;
		}

		/**
		 * @return the mapaAdFVaSpTrtoGa
		 */
		public Map<Long, List<RcsorAdFVaSpTrtoGa>> getMapaAdFVaSpTrtoGa() {
			return mapaAdFVaSpTrtoGa;
		}

		/**
		 * @param mapaAdFVaSpTrtoGa
		 *            the mapaAdFVaSpTrtoGa to set
		 */
		public void setMapaAdFVaSpTrtoGa(Map<Long, List<RcsorAdFVaSpTrtoGa>> mapaAdFVaSpTrtoGa) {
			this.mapaAdFVaSpTrtoGa = mapaAdFVaSpTrtoGa;
		}

		/**
		 * @return the mapaAdapFacVaSusby
		 */
		public Map<Long, List<RcsorAdapFacVaSusby>> getMapaAdapFacVaSusby() {
			return mapaAdapFacVaSusby;
		}

		/**
		 * @param mapaAdapFacVaSusby
		 *            the mapaAdapFacVaSusby to set
		 */
		public void setMapaAdapFacVaSusby(Map<Long, List<RcsorAdapFacVaSusby>> mapaAdapFacVaSusby) {
			this.mapaAdapFacVaSusby = mapaAdapFacVaSusby;
		}

		/**
		 * @return the mapaAdFaVaPsChVa
		 */
		public Map<Long, List<RcsorAdFaVaPsChVa>> getMapaAdFaVaPsChVa() {
			return mapaAdFaVaPsChVa;
		}

		/**
		 * @param mapaAdFaVaPsChVa
		 *            the mapaAdFaVaPsChVa to set
		 */
		public void setMapaAdFaVaPsChVa(Map<Long, List<RcsorAdFaVaPsChVa>> mapaAdFaVaPsChVa) {
			this.mapaAdFaVaPsChVa = mapaAdFaVaPsChVa;
		}

		/**
		 * @return the mapaAdFVaSpTrtoSsc
		 */
		public Map<Long, List<RcsorAdFVaSpTrtoSsc>> getMapaAdFVaSpTrtoSsc() {
			return mapaAdFVaSpTrtoSsc;
		}

		/**
		 * @param mapaAdFVaSpTrtoSsc
		 *            the mapaAdFVaSpTrtoSsc to set
		 */
		public void setMapaAdFVaSpTrtoSsc(Map<Long, List<RcsorAdFVaSpTrtoSsc>> mapaAdFVaSpTrtoSsc) {
			this.mapaAdFVaSpTrtoSsc = mapaAdFVaSpTrtoSsc;
		}

		/**
		 * @return the mapaAdFVaSpTrtoRsc
		 */
		public Map<Long, List<RcsorAdFVaSpTrtoRsc>> getMapaAdFVaSpTrtoRsc() {
			return mapaAdFVaSpTrtoRsc;
		}

		/**
		 * @param mapaAdFVaSpTrtoRsc
		 *            the mapaAdFVaSpTrtoRsc to set
		 */
		public void setMapaAdFVaSpTrtoRsc(Map<Long, List<RcsorAdFVaSpTrtoRsc>> mapaAdFVaSpTrtoRsc) {
			this.mapaAdFVaSpTrtoRsc = mapaAdFVaSpTrtoRsc;
		}

		/**
		 * @return the mapaAdNssHasFacVal
		 */
		public Map<Long, List<RcsorAdNssHasFacVal>> getMapaAdNssHasFacVal() {
			return mapaAdNssHasFacVal;
		}

		/**
		 * @param mapaAdNssHasFacVal
		 *            the mapaAdNssHasFacVal to set
		 */
		public void setMapaAdNssHasFacVal(Map<Long, List<RcsorAdNssHasFacVal>> mapaAdNssHasFacVal) {
			this.mapaAdNssHasFacVal = mapaAdNssHasFacVal;
		}

		/**
		 * @return the mapaAdFacSpTrSeSCv
		 */
		public Map<String, List<RcsorAdFacSpTrSeSCv>> getMapaAdFacSpTrSeSCv() {
			return mapaAdFacSpTrSeSCv;
		}

		/**
		 * @param mapaAdFacSpTrSeSCv
		 *            the mapaAdFacSpTrSeSCv to set
		 */
		public void setMapaAdFacSpTrSeSCv(Map<String, List<RcsorAdFacSpTrSeSCv>> mapaAdFacSpTrSeSCv) {
			this.mapaAdFacSpTrSeSCv = mapaAdFacSpTrSeSCv;
		}

		/**
		 * @return the mapaAdFacSpTrtoEi
		 */
		public Map<String, List<RcsorAdFacSpTrtoEi>> getMapaAdFacSpTrtoEi() {
			return mapaAdFacSpTrtoEi;
		}

		/**
		 * @param mapaAdFacSpTrtoEi
		 *            the mapaAdFacSpTrtoEi to set
		 */
		public void setMapaAdFacSpTrtoEi(Map<String, List<RcsorAdFacSpTrtoEi>> mapaAdFacSpTrtoEi) {
			this.mapaAdFacSpTrtoEi = mapaAdFacSpTrtoEi;
		}

		/**
		 * @return the mapaAdapFacSpRel
		 */
		public Map<String, List<RcsorAdapFacSpRel>> getMapaAdapFacSpRel() {
			return mapaAdapFacSpRel;
		}

		/**
		 * @param mapaAdapFacSpRel
		 *            the mapaAdapFacSpRel to set
		 */
		public void setMapaAdapFacSpRel(Map<String, List<RcsorAdapFacSpRel>> mapaAdapFacSpRel) {
			this.mapaAdapFacSpRel = mapaAdapFacSpRel;
		}

		/**
		 * @return the mapaAdFacValueSpec
		 */
		public Map<String, List<RcsopAdFacValueSpec>> getMapaAdFacValueSpec() {
			return mapaAdFacValueSpec;
		}

		/**
		 * @param mapaAdFacValueSpec
		 *            the mapaAdFacValueSpec to set
		 */
		public void setMapaAdFacValueSpec(Map<String, List<RcsopAdFacValueSpec>> mapaAdFacValueSpec) {
			this.mapaAdFacValueSpec = mapaAdFacValueSpec;
		}

		/**
		 * @return the mapaAdapTransportSpec
		 */
		public Map<String, List<RcsopAdapTransportSpec>> getMapaAdapTransportSpec() {
			return mapaAdapTransportSpec;
		}

		/**
		 * @param mapaAdapTransportSpec
		 *            the mapaAdapTransportSpec to set
		 */
		public void setMapaAdapTransportSpec(Map<String, List<RcsopAdapTransportSpec>> mapaAdapTransportSpec) {
			this.mapaAdapTransportSpec = mapaAdapTransportSpec;
		}

		/**
		 * @return the mapaAdapTrailSp
		 */
		public Map<String, RcsopAdapTrailSp> getMapaAdapTrailSp() {
			return mapaAdapTrailSp;
		}

		/**
		 * @param mapaAdapTrailSp
		 *            the mapaAdapTrailSp to set
		 */
		public void setMapaAdapTrailSp(Map<String, RcsopAdapTrailSp> mapaAdapTrailSp) {
			this.mapaAdapTrailSp = mapaAdapTrailSp;
		}

		/**
		 * @return the mapaAdapNetTrailSp
		 */
		public Map<String, List<RcsopAdapNetTrailSp>> getMapaAdapNetTrailSp() {
			return mapaAdapNetTrailSp;
		}

		/**
		 * @param mapaAdapNetTrailSp
		 *            the mapaAdapNetTrailSp to set
		 */
		public void setMapaAdapNetTrailSp(Map<String, List<RcsopAdapNetTrailSp>> mapaAdapNetTrailSp) {
			this.mapaAdapNetTrailSp = mapaAdapNetTrailSp;
		}

		/**
		 * @return the mapaAdNetTrUseFaSp
		 */
		public Map<String, List<RcsorAdNetTrUseFaSp>> getMapaAdNetTrUseFaSp() {
			return mapaAdNetTrUseFaSp;
		}

		/**
		 * @param mapaAdNetTrUseFaSp
		 *            the mapaAdNetTrUseFaSp to set
		 */
		public void setMapaAdNetTrUseFaSp(Map<String, List<RcsorAdNetTrUseFaSp>> mapaAdNetTrUseFaSp) {
			this.mapaAdNetTrUseFaSp = mapaAdNetTrUseFaSp;
		}

		/**
		 * @return the mapaAdNetTrUseEqRo
		 */
		public Map<String, List<RcsorAdNetTrUseEqRo>> getMapaAdNetTrUseEqRo() {
			return mapaAdNetTrUseEqRo;
		}

		/**
		 * @param mapaAdNetTrUseEqRo
		 *            the mapaAdNetTrUseEqRo to set
		 */
		public void setMapaAdNetTrUseEqRo(Map<String, List<RcsorAdNetTrUseEqRo>> mapaAdNetTrUseEqRo) {
			this.mapaAdNetTrUseEqRo = mapaAdNetTrUseEqRo;
		}

		/**
		 * @return the mapaAdTrailSpULrSp
		 */
		public Map<String, List<RcsorAdTrailSpULrSp>> getMapaAdTrailSpULrSp() {
			return mapaAdTrailSpULrSp;
		}

		/**
		 * @param mapaAdTrailSpULrSp
		 *            the mapaAdTrailSpULrSp to set
		 */
		public void setMapaAdTrailSpULrSp(Map<String, List<RcsorAdTrailSpULrSp>> mapaAdTrailSpULrSp) {
			this.mapaAdTrailSpULrSp = mapaAdTrailSpULrSp;
		}

		/**
		 * @return the mapaAdapEquipRoleSp
		 */
		public Map<String, List<RcsopAdapEquipRoleSp>> getMapaAdapEquipRoleSp() {
			return mapaAdapEquipRoleSp;
		}

		/**
		 * @param mapaAdapEquipRoleSp
		 *            the mapaAdapEquipRoleSp to set
		 */
		public void setMapaAdapEquipRoleSp(Map<String, List<RcsopAdapEquipRoleSp>> mapaAdapEquipRoleSp) {
			this.mapaAdapEquipRoleSp = mapaAdapEquipRoleSp;
		}

		/**
		 * @return the mapaAdEqRoRelIsTriBy
		 */
		public Map<String, List<RcsorAdEqRoRelIsTriBy>> getMapaAdEqRoRelIsTriBy() {
			return mapaAdEqRoRelIsTriBy;
		}

		/**
		 * @param mapaAdEqRoRelIsTriBy
		 *            the mapaAdEqRoRelIsTriBy to set
		 */
		public void setMapaAdEqRoRelIsTriBy(Map<String, List<RcsorAdEqRoRelIsTriBy>> mapaAdEqRoRelIsTriBy) {
			this.mapaAdEqRoRelIsTriBy = mapaAdEqRoRelIsTriBy;
		}

		/**
		 * @return the mapaAdEqRoUseFacSp
		 */
		public Map<Long, List<RcsorAdEqRoUseFacSp>> getMapaAdEqRoUseFacSp() {
			return mapaAdEqRoUseFacSp;
		}

		/**
		 * @param mapaAdEqRoUseFacSp
		 *            the mapaAdEqRoUseFacSp to set
		 */
		public void setMapaAdEqRoUseFacSp(Map<Long, List<RcsorAdEqRoUseFacSp>> mapaAdEqRoUseFacSp) {
			this.mapaAdEqRoUseFacSp = mapaAdEqRoUseFacSp;
		}

		/**
		 * @return the mapaNeLayHEqRole
		 */
		public Map<String, RcsorNeLayHEqRole> getMapaNeLayHEqRole() {
			return mapaNeLayHEqRole;
		}

		/**
		 * @param mapaNeLayHEqRole
		 *            the mapaNeLayHEqRole to set
		 */
		public void setMapaNeLayHEqRole(Map<String, RcsorNeLayHEqRole> mapaNeLayHEqRole) {
			this.mapaNeLayHEqRole = mapaNeLayHEqRole;
		}

		/**
		 * @return the mapaAdapParameter
		 */
		public Map<String, RcsopAdapParameter> getMapaAdapParameter() {
			return mapaAdapParameter;
		}

		/**
		 * @param mapaAdapParameter
		 *            the mapaAdapParameter to set
		 */
		public void setMapaAdapParameter(Map<String, RcsopAdapParameter> mapaAdapParameter) {
			this.mapaAdapParameter = mapaAdapParameter;
		}

		/**
		 * @return the mapaNetLayUseAc
		 */
		public Map<String, List<RcsorNetLayUseAc>> getMapaNetLayUseAc() {
			return mapaNetLayUseAc;
		}

		/**
		 * @param mapaNetLayUseAc
		 *            the mapaNetLayUseAc to set
		 */
		public void setMapaNetLayUseAc(Map<String, List<RcsorNetLayUseAc>> mapaNetLayUseAc) {
			this.mapaNetLayUseAc = mapaNetLayUseAc;
		}

		/**
		 * @return the mapaNetLayerHasFac
		 */
		public Map<String, List<RcsopAdapFacSpec>> getMapaNetLayerHasFac() {
			return mapaNetLayerHasFac;
		}

		/**
		 * @param mapaNetLayerHasFac
		 *            the mapaNetLayerHasFac to set
		 */
		public void setMapaNetLayerHasFac(Map<String, List<RcsopAdapFacSpec>> mapaNetLayerHasFac) {
			this.mapaNetLayerHasFac = mapaNetLayerHasFac;
		}

		/**
		 * @return the mapaNetLayerHTrail
		 */
		public Map<String, List<RcsopAdapTrailSp>> getMapaNetLayerHTrail() {
			return mapaNetLayerHTrail;
		}

		/**
		 * @param mapaNetLayerHTrail
		 *            the mapaNetLayerHTrail to set
		 */
		public void setMapaNetLayerHTrail(Map<String, List<RcsopAdapTrailSp>> mapaNetLayerHTrail) {
			this.mapaNetLayerHTrail = mapaNetLayerHTrail;
		}

		/**
		 * @return the mapaNeLayerHNetServ
		 */
		public Map<String, List<RcsopAdapNetSerSp>> getMapaNeLayerHNetServ() {
			return mapaNeLayerHNetServ;
		}

		/**
		 * @param mapaNeLayerHNetServ
		 *            the mapaNeLayerHNetServ to set
		 */
		public void setMapaNeLayerHNetServ(Map<String, List<RcsopAdapNetSerSp>> mapaNeLayerHNetServ) {
			this.mapaNeLayerHNetServ = mapaNeLayerHNetServ;
		}

		/**
		 * @param mapaAdapOperationType
		 *            the mapaAdapOperationType to set
		 */
		public void setMapaAdapFacilitySpecDTOOUT(
				Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT> mapaAdapOperationType) {
			this.mapaAdapFacilitySpecDTOOUT = mapaAdapOperationType;
		}

		/**
		 * @return the mapaAdapOperation
		 */
		public Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT> getMapaAdapFacilitySpecDTOOUT() {
			return mapaAdapFacilitySpecDTOOUT;
		}

		public Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT> getMapaAdapNetworkServiceSpecDTO() {
			return mapaAdapNetworkServiceSpecDTO;
		}

		public void setMapaAdapNetworkServiceSpecDTO(
				Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT> mapaAdapNetworkServiceSpecDTO) {
			this.mapaAdapNetworkServiceSpecDTO = mapaAdapNetworkServiceSpecDTO;
		}

		public Map<String, List<RcsoaAdTaskStepSp>> getMapaRcsorAdNSeSpUseT() {
			return mapaRcsorAdNSeSpUseT;
		}

		public void setMapaRcsorAdNSeSpUseT(Map<String, List<RcsoaAdTaskStepSp>> mapaRcsorAdNSeSpUseT) {
			this.mapaRcsorAdNSeSpUseT = mapaRcsorAdNSeSpUseT;
		}

		public Map<String, AdapTrailSpec_DTO_OUT> getMapaAdapTrailSpecDTO() {
			return mapaAdapTrailSpecDTO;
		}

		public void setMapaAdapTrailSpecDTO(Map<String, AdapTrailSpec_DTO_OUT> mapaAdapTrailSpecDTO) {
			this.mapaAdapTrailSpecDTO = mapaAdapTrailSpecDTO;
		}

		public Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapAccessSpec_DTO_OUT> getMapaAdapAccessSpec() {
			return mapaAdapAccessSpec;
		}

		public void setMapaAdapAccessSpec(
				Map<String, com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapAccessSpec_DTO_OUT> mapaAdapAccessSpec) {
			this.mapaAdapAccessSpec = mapaAdapAccessSpec;
		}

		public Map<String, AdapEquipmentRoleSpec_DTO_OUT> getMapaAdapEquipmentRoleSpecDTO() {
			return mapaAdapEquipmentRoleSpecDTO;
		}

		public void setMapaAdapEquipmentRoleSpecDTO(Map<String, AdapEquipmentRoleSpec_DTO_OUT> mapaAdapEquipmentRoleSpecDTO) {
			this.mapaAdapEquipmentRoleSpecDTO = mapaAdapEquipmentRoleSpecDTO;
		}
	}

	/**
	 * Clase para contener las diferentes listas obtenidas a partir de los
	 * findAll de las tablas necesarias
	 * 
	 * @author mnaharro
	 *
	 */
	class ListasFindAll {
		List<RcsopAdNetLayerSp> listaRcsopAdNetLayerSp;
		List<RcsodAdapOperationType> listaAdapOperationType;
		List<RcsopAdapOperation> listaAdapOperation;
		List<RcsoaAdOpTran> listaaAdOpTran;
		List<RcsodLockVoiceType> listaLockVoiceType;
		List<RcsorAdNetLaySpExeOp> listaAdNetLaySpExeOp;
		// Bloque 2: obtenAdapServiceLineSpec
		List<RcsopAdapSerLineSp> listaAdapSerLineSp;
		List<RcsorAdapSeLiTrRfsSp> listaAdapSeLiTrRfsSp;
		List<RcsorAdSeliSpUseNetse> listaAdSeliSpUseNetse;
		List<RcsorAdSerLiUseAcSp> listaAdSerLiUseAcSp;
		List<RcsorAdSlSpUseFaSp> listaAdSlSpUseFaSp;
		List<RcsorNetLayHasSeLine> listaNetLayHasSeLine;
		// Bloque 3: obtenAdapNetworkServiceSpec
		List<RcsopAdapNetSerSp> listaAdapNetSerSp;
		List<RcsodAdNetSeTypeSp> listaAdNetSeTypeSp;
		List<RcsorAdNetSSpSuby> listaAdNetSSpSuby;
		List<RcsorAdNSerTrRfsSp> listaAdNSerTrRfsSp;
		List<RcsorAdNetSSPTby> listaAdNetSSpTby;
		List<RcsorAdNetSSpTbyRec> listaAdNetSSpTbyRec;
		List<RcsorAdNSspUseFacSp> listaAdNSspUseFacSp;
		List<RcsorAdapNetSeSpRel> listaAdapNetSeSpRel;
		List<RcsorANetSeHEquRoSp> listaANetSeHEquRoSp;
		List<RcsodAdNetSerSpReTy> listaAdNetSerSpReTy;
		List<RcsoaAdTaskStepSp> listaAdTaskStepSp;
		List<RcsorAdNSeReqTrailSp> listaAdNSeReqTrailSp;
		List<RcsorAdapNssIsTrigBy> listaAdapNssIsTrigBy;
		List<RcsorAdNSeSpUseT> listaRcsorAdNSeSpUseT;
		// Bloque 4: obtenAdapAccesSpec
		List<RcsopAdapAccSpec> listaAdapAccSpec;
		List<RcsorAdAcSpURSpChVal> listaAdAcSpURSpChVal;
		List<RcsorAdAccSpULogRSp> listaAdAccSpULogRSp;
		List<RcsorAdAccUseEqRoSp> listaAdAccUseEqRoSp;
		List<RcsorAdAccUseFacSpec> listaAdAccUseFacSpec;
		// Bloque 5: obtenAdapFacilitySpec
		List<RcsodAdapFaSpecType> listaAdapFaSpecType;
		List<RcsopAdapFacSpec> listaAdapFacSpec;
		List<RcsorAdFaSHResS> listaAdFaSHResS;
		List<RcsorAdFaPsChVa> listaAdFaPsChVa;
		List<RcsorAdFacSpTrtoRsc> listaAdFacSpTrtoRsc;
		List<RcsorAdFVaSpTrtoGa> listaAdFVaSpTrtoGa;
		List<RcsorAdapFacVaSusby> listaAdapFacVaSusby;
		List<RcsorAdFaVaPsChVa> listaAdFaVaPsChVa;
		List<RcsorAdFVaSpTrtoSsc> listaAdFVaSpTrtoSsc;
		List<RcsorAdFVaSpTrtoRsc> listaAdFVaSpTrtoRsc;
		List<RcsorAdNssHasFacVal> listaAdNssHasFacVal;
		List<RcsorAdFacSpTrSeSCv> listaAdFacSpTrSeSCv;
		List<RcsorAdFacSpTrtoEi> listaAdFacSpTrtoEi;
		List<RcsorAdapFacSpRel> listaAdapFacSpRel;
		List<RcsopAdFacValueSpec> listaAdFacValueSpec;
		List<RcsorNetLayerHasFac> listaNetLayerHasFac;
		// Bloque 6: obtenAdapTrailSpec
		List<RcsopAdapTransportSpec> listaAdapTransportSpec;
		List<RcsopAdapTrailSp> listaAdapTrailSp;
		List<RcsopAdapNetTrailSp> listaAdapNetTrailSp;
		List<RcsorAdNetTrUseFaSp> listaAdNetTrUseFaSp;
		List<RcsorAdNetTrUseEqRo> listaAdNetTrUseEqRo;
		List<RcsorAdTrailSpULrSp> listaAdTrailSpULrSp;
		// Bloque 7: obtenAdapEquipmentRoleSpec
		List<RcsopAdapEquipRoleSp> listaAdapEquipRoleSp;
		List<RcsorAdEqRoRelIsTriBy> listaAdEqRoRelIsTriBy;
		List<RcsorAdEqRoUseFacSp> listaAdEqRoUseFacSp;
		List<RcsorNeLayHEqRole> listaNeLayHEqRole;
		// Bloque 8:
		List<RcsopAdapParameter> listaAdapParameter;
		List<RcsorNetLayUseAc> listaNetLayUseAc;
		List<RcsorNetLayerHTrail> listaNetLayerHTrail;
		List<RcsorNeLayerHNetServ> listaNeLayerHNetServ;

		ListasFindAll() {

			listaRcsopAdNetLayerSp = rcsopAdNetLayerSpRepository.findAll();
			// Bloque 1: obtenAdapNetLaySpExecutesOp
			listaAdapOperationType = rcsodAdapOperationTypeRepository.findAll();
			listaAdapOperation = rcsopAdapOperationRepository.findAll();
			listaaAdOpTran = rcsoaAdOpTranRepository.findAll();
			listaLockVoiceType = rcsodLockVoiceTypeRepository.findAll();
			listaAdNetLaySpExeOp = rcsorAdNetLaySpExeOpRepository.findAll();
			// Bloque 2: obtenAdapServiceLineSpec
			listaAdapSerLineSp = rcsopAdapSerLineSpRepository.findAll();
			listaAdapSeLiTrRfsSp = rcsorAdapSeLiTrRfsSpRepository.findAll();
			listaAdSeliSpUseNetse = rcsorAdSeliSpUseNetseRepository.findAll();
			listaAdSerLiUseAcSp = rcsorAdSerLiUseAcSpRepository.findAll();
			listaAdSlSpUseFaSp = rcsorAdSlSpUseFaSpRepository.findAll();
			listaNetLayHasSeLine = rcsorNetLayHasSeLineRepository.findAll();
			// Bloque 3: obtenAdapNetworkServiceSpec
			listaAdapNetSerSp = rcsopAdapNetSerSpRepository.findAll();
			listaAdNetSeTypeSp = rcsodAdNetSeTypeSpRepository.findAll();
			listaAdNetSSpSuby = rcsorAdNetSSpSubyRepository.findAll();
			listaAdNSerTrRfsSp = rcsorAdNSerTrRfsSpRepository.findAll();
			listaAdNetSSpTby = rcsorAdNetSSpTbyRepository.findAll();
			listaAdNetSSpTbyRec = rcsorAdNetSSpTbyRecRepository.findAll();
			listaAdNSspUseFacSp = rcsorAdNSspUseFacSpRepository.findAll();
			listaAdapNetSeSpRel = rcsorAdapNetSeSpRelRepository.findAll();
			listaANetSeHEquRoSp = rcsorANetSeHEquRoSpRepository.findAll();
			listaAdNetSerSpReTy = rcsodAdNetSerSpReTyRepository.findAll();
			listaAdTaskStepSp = rcsoaAdTaskStepSpRepository.findAll();
			listaAdNSeReqTrailSp = rcsorAdNSeReqTrailSpRepository.findAll();
			listaAdapNssIsTrigBy = rcsorAdapNssIsTrigByRepository.findAll();
			listaRcsorAdNSeSpUseT = rcsorAdNSeSpUseTRepository.findAll();

			// Bloque 4: obtenAdapAccesSpec
			listaAdapAccSpec = rcsopAdapAccSpecRepository.findAll();
			listaAdAcSpURSpChVal = rcsorAdAcSpURSpChValRepository.findAll();
			listaAdAccSpULogRSp = rcsorAdAccSpULogRSpRepository.findAll();
			listaAdAccUseEqRoSp = rcsorAdAccUseEqRoSpRepository.findAll();
			listaAdAccUseFacSpec = rcsorAdAccUseFacSpecRepository.findAll();
			// Bloque 5: obtenAdapFacilitySpec
			listaAdapFaSpecType = rcsodAdapFaSpecTypeRepository.findAll();
			listaAdapFacSpec = rcsopAdapFacSpecRepository.findAll();
			listaAdFaSHResS = rcsorAdFaSHResSRepository.findAll();
			listaAdFaPsChVa = rcsorAdFaPsChVaRepository.findAll();
			listaAdFacSpTrtoRsc = rcsorAdFacSpTrtoRscRepository.findAll();
			listaAdFVaSpTrtoGa = rcsorAdFVaSpTrtoGaRepository.findAll();
			listaAdapFacVaSusby = rcsorAdapFacVaSusbyRepository.findAll();
			listaAdFaVaPsChVa = rcsorAdFaVaPsChVaRepository.findAll();
			listaAdFVaSpTrtoSsc = rcsorAdFVaSpTrtoSscRepository.findAll();
			listaAdFVaSpTrtoRsc = rcsorAdFVaSpTrtoRscRepository.findAll();
			listaAdNssHasFacVal = rcsorAdNssHasFacValRepository.findAll();
			listaAdFacSpTrSeSCv = rcsorAdFacSpTrSeSCvRepository.findAll();
			listaAdFacSpTrtoEi = rcsorAdFacSpTrtoEiRepository.findAll();
			listaAdapFacSpRel = rcsorAdapFacSpRelRepository.findAll();
			listaAdFacValueSpec = rcsopAdFacValueSpecRepository.findAll();
			// Bloque 6: obtenAdapTrailSpec
			listaAdapTransportSpec = rcsopAdapTransportSpecRepository.findAll();
			listaAdapTrailSp = rcsopAdapTrailSpRepository.findAll();
			listaAdapNetTrailSp = rcsopAdapNetTrailSpRepository.findAll();
			listaAdNetTrUseFaSp = rcsorAdNetTrUseFaSpRepository.findAll();
			listaAdNetTrUseEqRo = rcsorAdNetTrUseEqRoRepository.findAll();
			listaAdTrailSpULrSp = rcsorAdTrailSpULrSpRepository.findAll();
			listaNetLayerHasFac = rcsorNetLayerHasFacRepository.findAll();
			// Bloque 7: obtenAdapEquipmentRoleSpec
			listaAdapEquipRoleSp = rcsopAdapEquipRoleSpRepository.findAll();
			listaAdEqRoRelIsTriBy = rcsorAdEqRoRelIsTriByRepository.findAll();
			listaAdEqRoUseFacSp = rcsorAdEqRoUseFacSpRepository.findAll();
			listaNeLayHEqRole = rcsorNeLayHEqRoleRepository.findAll();
			// Bloque 8:
			listaAdapParameter = rcsopAdapParameterRepository.findAll();
			listaNetLayUseAc = rcsorNetLayUseAcRepository.findAll();
			listaNetLayerHTrail = rcsorNetLayerHTrailRepository.findAll();
			listaNeLayerHNetServ = rcsorNeLayerHNetServRepository.findAll();

		}

		/**
		 * @return the listaAdapOperationType
		 */
		public List<RcsopAdNetLayerSp> getListaRcsopAdNetLayerSp() {
			return listaRcsopAdNetLayerSp;
		}

		/**
		 * @param listaAdapOperationType
		 *            the listaAdapOperationType to set
		 */
		public void setListaRcsopAdNetLayerSp(List<RcsopAdNetLayerSp> listaRcsopAdNetLayerSp) {
			this.listaRcsopAdNetLayerSp = listaRcsopAdNetLayerSp;
		}

		/**
		 * @return the listaAdapOperationType
		 */
		public List<RcsodAdapOperationType> getListaAdapOperationType() {
			return listaAdapOperationType;
		}

		/**
		 * @param listaAdapOperationType
		 *            the listaAdapOperationType to set
		 */
		public void setListaAdapOperationType(List<RcsodAdapOperationType> listaAdapOperationType) {
			this.listaAdapOperationType = listaAdapOperationType;
		}

		/**
		 * @return the listaAdapOperation
		 */
		public List<RcsopAdapOperation> getListaAdapOperation() {
			return listaAdapOperation;
		}

		/**
		 * @param listaAdapOperation
		 *            the listaAdapOperation to set
		 */
		public void setListaAdapOperation(List<RcsopAdapOperation> listaAdapOperation) {
			this.listaAdapOperation = listaAdapOperation;
		}

		/**
		 * @return the listaaAdOpTran
		 */
		public List<RcsoaAdOpTran> getListaaAdOpTran() {
			return listaaAdOpTran;
		}

		/**
		 * @param listaaAdOpTran
		 *            the listaaAdOpTran to set
		 */
		public void setListaaAdOpTran(List<RcsoaAdOpTran> listaaAdOpTran) {
			this.listaaAdOpTran = listaaAdOpTran;
		}

		/**
		 * @return the listaLockVoiceType
		 */
		public List<RcsodLockVoiceType> getListaLockVoiceType() {
			return listaLockVoiceType;
		}

		/**
		 * @param listaLockVoiceType
		 *            the listaLockVoiceType to set
		 */
		public void setListaLockVoiceType(List<RcsodLockVoiceType> listaLockVoiceType) {
			this.listaLockVoiceType = listaLockVoiceType;
		}

		/**
		 * @return the listaAdNetLaySpExeOp
		 */
		public List<RcsorAdNetLaySpExeOp> getListaAdNetLaySpExeOp() {
			return listaAdNetLaySpExeOp;
		}

		/**
		 * @param listaAdNetLaySpExeOp
		 *            the listaAdNetLaySpExeOp to set
		 */
		public void setListaAdNetLaySpExeOp(List<RcsorAdNetLaySpExeOp> listaAdNetLaySpExeOp) {
			this.listaAdNetLaySpExeOp = listaAdNetLaySpExeOp;
		}

		/**
		 * @return the listaAdapSerLineSp
		 */
		public List<RcsopAdapSerLineSp> getListaAdapSerLineSp() {
			return listaAdapSerLineSp;
		}

		/**
		 * @param listaAdapSerLineSp
		 *            the listaAdapSerLineSp to set
		 */
		public void setListaAdapSerLineSp(List<RcsopAdapSerLineSp> listaAdapSerLineSp) {
			this.listaAdapSerLineSp = listaAdapSerLineSp;
		}

		/**
		 * @return the listaAdapSeLiTrRfsSp
		 */
		public List<RcsorAdapSeLiTrRfsSp> getListaAdapSeLiTrRfsSp() {
			return listaAdapSeLiTrRfsSp;
		}

		/**
		 * @param listaAdapSeLiTrRfsSp
		 *            the listaAdapSeLiTrRfsSp to set
		 */
		public void setListaAdapSeLiTrRfsSp(List<RcsorAdapSeLiTrRfsSp> listaAdapSeLiTrRfsSp) {
			this.listaAdapSeLiTrRfsSp = listaAdapSeLiTrRfsSp;
		}

		/**
		 * @return the listaAdSeliSpUseNetse
		 */
		public List<RcsorAdSeliSpUseNetse> getListaAdSeliSpUseNetse() {
			return listaAdSeliSpUseNetse;
		}

		/**
		 * @param listaAdSeliSpUseNetse
		 *            the listaAdSeliSpUseNetse to set
		 */
		public void setListaAdSeliSpUseNetse(List<RcsorAdSeliSpUseNetse> listaAdSeliSpUseNetse) {
			this.listaAdSeliSpUseNetse = listaAdSeliSpUseNetse;
		}

		/**
		 * @return the listaAdSerLiUseAcSp
		 */
		public List<RcsorAdSerLiUseAcSp> getListaAdSerLiUseAcSp() {
			return listaAdSerLiUseAcSp;
		}

		/**
		 * @param listaAdSerLiUseAcSp
		 *            the listaAdSerLiUseAcSp to set
		 */
		public void setListaAdSerLiUseAcSp(List<RcsorAdSerLiUseAcSp> listaAdSerLiUseAcSp) {
			this.listaAdSerLiUseAcSp = listaAdSerLiUseAcSp;
		}

		/**
		 * @return the listaAdSlSpUseFaSp
		 */
		public List<RcsorAdSlSpUseFaSp> getListaAdSlSpUseFaSp() {
			return listaAdSlSpUseFaSp;
		}

		/**
		 * @param listaAdSlSpUseFaSp
		 *            the listaAdSlSpUseFaSp to set
		 */
		public void setListaAdSlSpUseFaSp(List<RcsorAdSlSpUseFaSp> listaAdSlSpUseFaSp) {
			this.listaAdSlSpUseFaSp = listaAdSlSpUseFaSp;
		}

		/**
		 * @return the listaNetLayHasSeLine
		 */
		public List<RcsorNetLayHasSeLine> getListaNetLayHasSeLine() {
			return listaNetLayHasSeLine;
		}

		/**
		 * @param listaNetLayHasSeLine
		 *            the listaNetLayHasSeLine to set
		 */
		public void setListaNetLayHasSeLine(List<RcsorNetLayHasSeLine> listaNetLayHasSeLine) {
			this.listaNetLayHasSeLine = listaNetLayHasSeLine;
		}

		/**
		 * @return the listaAdapNetSerSp
		 */
		public List<RcsopAdapNetSerSp> getListaAdapNetSerSp() {
			return listaAdapNetSerSp;
		}

		/**
		 * @param listaAdapNetSerSp
		 *            the listaAdapNetSerSp to set
		 */
		public void setListaAdapNetSerSp(List<RcsopAdapNetSerSp> listaAdapNetSerSp) {
			this.listaAdapNetSerSp = listaAdapNetSerSp;
		}

		/**
		 * @return the listaAdNetSeTypeSp
		 */
		public List<RcsodAdNetSeTypeSp> getListaAdNetSeTypeSp() {
			return listaAdNetSeTypeSp;
		}

		/**
		 * @param listaAdNetSeTypeSp
		 *            the listaAdNetSeTypeSp to set
		 */
		public void setListaAdNetSeTypeSp(List<RcsodAdNetSeTypeSp> listaAdNetSeTypeSp) {
			this.listaAdNetSeTypeSp = listaAdNetSeTypeSp;
		}

		/**
		 * @return the listaAdNetSSpSuby
		 */
		public List<RcsorAdNetSSpSuby> getListaAdNetSSpSuby() {
			return listaAdNetSSpSuby;
		}

		/**
		 * @param listaAdNetSSpSuby
		 *            the listaAdNetSSpSuby to set
		 */
		public void setListaAdNetSSpSuby(List<RcsorAdNetSSpSuby> listaAdNetSSpSuby) {
			this.listaAdNetSSpSuby = listaAdNetSSpSuby;
		}

		/**
		 * @return the listaAdNSerTrRfsSp
		 */
		public List<RcsorAdNSerTrRfsSp> getListaAdNSerTrRfsSp() {
			return listaAdNSerTrRfsSp;
		}

		/**
		 * @param listaAdNSerTrRfsSp
		 *            the listaAdNSerTrRfsSp to set
		 */
		public void setListaAdNSerTrRfsSp(List<RcsorAdNSerTrRfsSp> listaAdNSerTrRfsSp) {
			this.listaAdNSerTrRfsSp = listaAdNSerTrRfsSp;
		}

		/**
		 * @return the listaAdNetSSpTby
		 */
		public List<RcsorAdNetSSPTby> getListaAdNetSSpTby() {
			return listaAdNetSSpTby;
		}

		/**
		 * @param listaAdNetSSpTby
		 *            the listaAdNetSSpTby to set
		 */
		public void setListaAdNetSSpTby(List<RcsorAdNetSSPTby> listaAdNetSSpTby) {
			this.listaAdNetSSpTby = listaAdNetSSpTby;
		}

		/**
		 * @return the listaAdNetSSpTbyRec
		 */
		public List<RcsorAdNetSSpTbyRec> getListaAdNetSSpTbyRec() {
			return listaAdNetSSpTbyRec;
		}

		/**
		 * @param listaAdNetSSpTbyRec
		 *            the listaAdNetSSpTbyRec to set
		 */
		public void setListaAdNetSSpTbyRec(List<RcsorAdNetSSpTbyRec> listaAdNetSSpTbyRec) {
			this.listaAdNetSSpTbyRec = listaAdNetSSpTbyRec;
		}

		/**
		 * @return the listaAdNSspUseFacSp
		 */
		public List<RcsorAdNSspUseFacSp> getListaAdNSspUseFacSp() {
			return listaAdNSspUseFacSp;
		}

		/**
		 * @param listaAdNSspUseFacSp
		 *            the listaAdNSspUseFacSp to set
		 */
		public void setListaAdNSspUseFacSp(List<RcsorAdNSspUseFacSp> listaAdNSspUseFacSp) {
			this.listaAdNSspUseFacSp = listaAdNSspUseFacSp;
		}

		/**
		 * @return the listaAdapNetSeSpRel
		 */
		public List<RcsorAdapNetSeSpRel> getListaAdapNetSeSpRel() {
			return listaAdapNetSeSpRel;
		}

		/**
		 * @param listaAdapNetSeSpRel
		 *            the listaAdapNetSeSpRel to set
		 */
		public void setListaAdapNetSeSpRel(List<RcsorAdapNetSeSpRel> listaAdapNetSeSpRel) {
			this.listaAdapNetSeSpRel = listaAdapNetSeSpRel;
		}

		/**
		 * @return the listaANetSeHEquRoSp
		 */
		public List<RcsorANetSeHEquRoSp> getListaANetSeHEquRoSp() {
			return listaANetSeHEquRoSp;
		}

		/**
		 * @param listaANetSeHEquRoSp
		 *            the listaANetSeHEquRoSp to set
		 */
		public void setListaANetSeHEquRoSp(List<RcsorANetSeHEquRoSp> listaANetSeHEquRoSp) {
			this.listaANetSeHEquRoSp = listaANetSeHEquRoSp;
		}

		/**
		 * @return the listaAdNetSerSpReTy
		 */
		public List<RcsodAdNetSerSpReTy> getListaAdNetSerSpReTy() {
			return listaAdNetSerSpReTy;
		}

		/**
		 * @param listaAdNetSerSpReTy
		 *            the listaAdNetSerSpReTy to set
		 */
		public void setListaAdNetSerSpReTy(List<RcsodAdNetSerSpReTy> listaAdNetSerSpReTy) {
			this.listaAdNetSerSpReTy = listaAdNetSerSpReTy;
		}

		/**
		 * @return the listaAdTaskStepSp
		 */
		public List<RcsoaAdTaskStepSp> getListaAdTaskStepSp() {
			return listaAdTaskStepSp;
		}

		/**
		 * @param listaAdTaskStepSp
		 *            the listaAdTaskStepSp to set
		 */
		public void setListaAdTaskStepSp(List<RcsoaAdTaskStepSp> listaAdTaskStepSp) {
			this.listaAdTaskStepSp = listaAdTaskStepSp;
		}

		/**
		 * @return the listaAdNSeReqTrailSp
		 */
		public List<RcsorAdNSeReqTrailSp> getListaAdNSeReqTrailSp() {
			return listaAdNSeReqTrailSp;
		}

		/**
		 * @param listaAdNSeReqTrailSp
		 *            the listaAdNSeReqTrailSp to set
		 */
		public void setListaAdNSeReqTrailSp(List<RcsorAdNSeReqTrailSp> listaAdNSeReqTrailSp) {
			this.listaAdNSeReqTrailSp = listaAdNSeReqTrailSp;
		}

		/**
		 * @return the listaAdapNssIsTrigBy
		 */
		public List<RcsorAdapNssIsTrigBy> getListaAdapNssIsTrigBy() {
			return listaAdapNssIsTrigBy;
		}

		/**
		 * @param listaAdapNssIsTrigBy
		 *            the listaAdapNssIsTrigBy to set
		 */
		public void setListaAdapNssIsTrigBy(List<RcsorAdapNssIsTrigBy> listaAdapNssIsTrigBy) {
			this.listaAdapNssIsTrigBy = listaAdapNssIsTrigBy;
		}

		/**
		 * @return the listaAdapAccSpec
		 */
		public List<RcsopAdapAccSpec> getListaAdapAccSpec() {
			return listaAdapAccSpec;
		}

		/**
		 * @param listaAdapAccSpec
		 *            the listaAdapAccSpec to set
		 */
		public void setListaAdapAccSpec(List<RcsopAdapAccSpec> listaAdapAccSpec) {
			this.listaAdapAccSpec = listaAdapAccSpec;
		}

		/**
		 * @return the listaAdAcSpURSpChVal
		 */
		public List<RcsorAdAcSpURSpChVal> getListaAdAcSpURSpChVal() {
			return listaAdAcSpURSpChVal;
		}

		/**
		 * @param listaAdAcSpURSpChVal
		 *            the listaAdAcSpURSpChVal to set
		 */
		public void setListaAdAcSpURSpChVal(List<RcsorAdAcSpURSpChVal> listaAdAcSpURSpChVal) {
			this.listaAdAcSpURSpChVal = listaAdAcSpURSpChVal;
		}

		/**
		 * @return the listaAdAccSpULogRSp
		 */
		public List<RcsorAdAccSpULogRSp> getListaAdAccSpULogRSp() {
			return listaAdAccSpULogRSp;
		}

		/**
		 * @param listaAdAccSpULogRSp
		 *            the listaAdAccSpULogRSp to set
		 */
		public void setListaAdAccSpULogRSp(List<RcsorAdAccSpULogRSp> listaAdAccSpULogRSp) {
			this.listaAdAccSpULogRSp = listaAdAccSpULogRSp;
		}

		/**
		 * @return the listaAdAccUseEqRoSp
		 */
		public List<RcsorAdAccUseEqRoSp> getListaAdAccUseEqRoSp() {
			return listaAdAccUseEqRoSp;
		}

		/**
		 * @param listaAdAccUseEqRoSp
		 *            the listaAdAccUseEqRoSp to set
		 */
		public void setListaAdAccUseEqRoSp(List<RcsorAdAccUseEqRoSp> listaAdAccUseEqRoSp) {
			this.listaAdAccUseEqRoSp = listaAdAccUseEqRoSp;
		}

		/**
		 * @return the listaAdAccUseFacSpec
		 */
		public List<RcsorAdAccUseFacSpec> getListaAdAccUseFacSpec() {
			return listaAdAccUseFacSpec;
		}

		/**
		 * @param listaAdAccUseFacSpec
		 *            the listaAdAccUseFacSpec to set
		 */
		public void setListaAdAccUseFacSpec(List<RcsorAdAccUseFacSpec> listaAdAccUseFacSpec) {
			this.listaAdAccUseFacSpec = listaAdAccUseFacSpec;
		}

		/**
		 * @return the listaAdapFaSpecType
		 */
		public List<RcsodAdapFaSpecType> getListaAdapFaSpecType() {
			return listaAdapFaSpecType;
		}

		/**
		 * @param listaAdapFaSpecType
		 *            the listaAdapFaSpecType to set
		 */
		public void setListaAdapFaSpecType(List<RcsodAdapFaSpecType> listaAdapFaSpecType) {
			this.listaAdapFaSpecType = listaAdapFaSpecType;
		}

		/**
		 * @return the listaAdapFacSpec
		 */
		public List<RcsopAdapFacSpec> getListaAdapFacSpec() {
			return listaAdapFacSpec;
		}

		/**
		 * @param listaAdapFacSpec
		 *            the listaAdapFacSpec to set
		 */
		public void setListaAdapFacSpec(List<RcsopAdapFacSpec> listaAdapFacSpec) {
			this.listaAdapFacSpec = listaAdapFacSpec;
		}

		/**
		 * @return the listaAdFaSHResS
		 */
		public List<RcsorAdFaSHResS> getListaAdFaSHResS() {
			return listaAdFaSHResS;
		}

		/**
		 * @param listaAdFaSHResS
		 *            the listaAdFaSHResS to set
		 */
		public void setListaAdFaSHResS(List<RcsorAdFaSHResS> listaAdFaSHResS) {
			this.listaAdFaSHResS = listaAdFaSHResS;
		}

		/**
		 * @return the listaAdFaPsChVa
		 */
		public List<RcsorAdFaPsChVa> getListaAdFaPsChVa() {
			return listaAdFaPsChVa;
		}

		/**
		 * @param listaAdFaPsChVa
		 *            the listaAdFaPsChVa to set
		 */
		public void setListaAdFaPsChVa(List<RcsorAdFaPsChVa> listaAdFaPsChVa) {
			this.listaAdFaPsChVa = listaAdFaPsChVa;
		}

		/**
		 * @return the listaAdFacSpTrtoRsc
		 */
		public List<RcsorAdFacSpTrtoRsc> getListaAdFacSpTrtoRsc() {
			return listaAdFacSpTrtoRsc;
		}

		/**
		 * @param listaAdFacSpTrtoRsc
		 *            the listaAdFacSpTrtoRsc to set
		 */
		public void setListaAdFacSpTrtoRsc(List<RcsorAdFacSpTrtoRsc> listaAdFacSpTrtoRsc) {
			this.listaAdFacSpTrtoRsc = listaAdFacSpTrtoRsc;
		}

		/**
		 * @return the listaAdFVaSpTrtoGa
		 */
		public List<RcsorAdFVaSpTrtoGa> getListaAdFVaSpTrtoGa() {
			return listaAdFVaSpTrtoGa;
		}

		/**
		 * @param listaAdFVaSpTrtoGa
		 *            the listaAdFVaSpTrtoGa to set
		 */
		public void setListaAdFVaSpTrtoGa(List<RcsorAdFVaSpTrtoGa> listaAdFVaSpTrtoGa) {
			this.listaAdFVaSpTrtoGa = listaAdFVaSpTrtoGa;
		}

		/**
		 * @return the listaAdapFacVaSusby
		 */
		public List<RcsorAdapFacVaSusby> getListaAdapFacVaSusby() {
			return listaAdapFacVaSusby;
		}

		/**
		 * @param listaAdapFacVaSusby
		 *            the listaAdapFacVaSusby to set
		 */
		public void setListaAdapFacVaSusby(List<RcsorAdapFacVaSusby> listaAdapFacVaSusby) {
			this.listaAdapFacVaSusby = listaAdapFacVaSusby;
		}

		/**
		 * @return the listaAdFaVaPsChVa
		 */
		public List<RcsorAdFaVaPsChVa> getListaAdFaVaPsChVa() {
			return listaAdFaVaPsChVa;
		}

		/**
		 * @param listaAdFaVaPsChVa
		 *            the listaAdFaVaPsChVa to set
		 */
		public void setListaAdFaVaPsChVa(List<RcsorAdFaVaPsChVa> listaAdFaVaPsChVa) {
			this.listaAdFaVaPsChVa = listaAdFaVaPsChVa;
		}

		/**
		 * @return the listaAdFVaSpTrtoSsc
		 */
		public List<RcsorAdFVaSpTrtoSsc> getListaAdFVaSpTrtoSsc() {
			return listaAdFVaSpTrtoSsc;
		}

		/**
		 * @param listaAdFVaSpTrtoSsc
		 *            the listaAdFVaSpTrtoSsc to set
		 */
		public void setListaAdFVaSpTrtoSsc(List<RcsorAdFVaSpTrtoSsc> listaAdFVaSpTrtoSsc) {
			this.listaAdFVaSpTrtoSsc = listaAdFVaSpTrtoSsc;
		}

		/**
		 * @return the listaAdFVaSpTrtoRsc
		 */
		public List<RcsorAdFVaSpTrtoRsc> getListaAdFVaSpTrtoRsc() {
			return listaAdFVaSpTrtoRsc;
		}

		/**
		 * @param listaAdFVaSpTrtoRsc
		 *            the listaAdFVaSpTrtoRsc to set
		 */
		public void setListaAdFVaSpTrtoRsc(List<RcsorAdFVaSpTrtoRsc> listaAdFVaSpTrtoRsc) {
			this.listaAdFVaSpTrtoRsc = listaAdFVaSpTrtoRsc;
		}

		/**
		 * @return the listaAdNssHasFacVal
		 */
		public List<RcsorAdNssHasFacVal> getListaAdNssHasFacVal() {
			return listaAdNssHasFacVal;
		}

		/**
		 * @param listaAdNssHasFacVal
		 *            the listaAdNssHasFacVal to set
		 */
		public void setListaAdNssHasFacVal(List<RcsorAdNssHasFacVal> listaAdNssHasFacVal) {
			this.listaAdNssHasFacVal = listaAdNssHasFacVal;
		}

		/**
		 * @return the listaAdFacSpTrSeSCv
		 */
		public List<RcsorAdFacSpTrSeSCv> getListaAdFacSpTrSeSCv() {
			return listaAdFacSpTrSeSCv;
		}

		/**
		 * @param listaAdFacSpTrSeSCv
		 *            the listaAdFacSpTrSeSCv to set
		 */
		public void setListaAdFacSpTrSeSCv(List<RcsorAdFacSpTrSeSCv> listaAdFacSpTrSeSCv) {
			this.listaAdFacSpTrSeSCv = listaAdFacSpTrSeSCv;
		}

		/**
		 * @return the listaAdFacSpTrtoEi
		 */
		public List<RcsorAdFacSpTrtoEi> getListaAdFacSpTrtoEi() {
			return listaAdFacSpTrtoEi;
		}

		/**
		 * @param listaAdFacSpTrtoEi
		 *            the listaAdFacSpTrtoEi to set
		 */
		public void setListaAdFacSpTrtoEi(List<RcsorAdFacSpTrtoEi> listaAdFacSpTrtoEi) {
			this.listaAdFacSpTrtoEi = listaAdFacSpTrtoEi;
		}

		/**
		 * @return the listaAdapFacSpRel
		 */
		public List<RcsorAdapFacSpRel> getListaAdapFacSpRel() {
			return listaAdapFacSpRel;
		}

		/**
		 * @param listaAdapFacSpRel
		 *            the listaAdapFacSpRel to set
		 */
		public void setListaAdapFacSpRel(List<RcsorAdapFacSpRel> listaAdapFacSpRel) {
			this.listaAdapFacSpRel = listaAdapFacSpRel;
		}

		/**
		 * @return the listaAdFacValueSpec
		 */
		public List<RcsopAdFacValueSpec> getListaAdFacValueSpec() {
			return listaAdFacValueSpec;
		}

		/**
		 * @param listaAdFacValueSpec
		 *            the listaAdFacValueSpec to set
		 */
		public void setListaAdFacValueSpec(List<RcsopAdFacValueSpec> listaAdFacValueSpec) {
			this.listaAdFacValueSpec = listaAdFacValueSpec;
		}

		/**
		 * @return the listaAdapTransportSpec
		 */
		public List<RcsopAdapTransportSpec> getListaAdapTransportSpec() {
			return listaAdapTransportSpec;
		}

		/**
		 * @param listaAdapTransportSpec
		 *            the listaAdapTransportSpec to set
		 */
		public void setListaAdapTransportSpec(List<RcsopAdapTransportSpec> listaAdapTransportSpec) {
			this.listaAdapTransportSpec = listaAdapTransportSpec;
		}

		/**
		 * @return the listaAdapTrailSp
		 */
		public List<RcsopAdapTrailSp> getListaAdapTrailSp() {
			return listaAdapTrailSp;
		}

		/**
		 * @param listaAdapTrailSp
		 *            the listaAdapTrailSp to set
		 */
		public void setListaAdapTrailSp(List<RcsopAdapTrailSp> listaAdapTrailSp) {
			this.listaAdapTrailSp = listaAdapTrailSp;
		}

		/**
		 * @return the listaAdapNetTrailSp
		 */
		public List<RcsopAdapNetTrailSp> getListaAdapNetTrailSp() {
			return listaAdapNetTrailSp;
		}

		/**
		 * @param listaAdapNetTrailSp
		 *            the listaAdapNetTrailSp to set
		 */
		public void setListaAdapNetTrailSp(List<RcsopAdapNetTrailSp> listaAdapNetTrailSp) {
			this.listaAdapNetTrailSp = listaAdapNetTrailSp;
		}

		/**
		 * @return the listaAdNetTrUseFaSp
		 */
		public List<RcsorAdNetTrUseFaSp> getListaAdNetTrUseFaSp() {
			return listaAdNetTrUseFaSp;
		}

		/**
		 * @param listaAdNetTrUseFaSp
		 *            the listaAdNetTrUseFaSp to set
		 */
		public void setListaAdNetTrUseFaSp(List<RcsorAdNetTrUseFaSp> listaAdNetTrUseFaSp) {
			this.listaAdNetTrUseFaSp = listaAdNetTrUseFaSp;
		}

		/**
		 * @return the listaAdNetTrUseEqRo
		 */
		public List<RcsorAdNetTrUseEqRo> getListaAdNetTrUseEqRo() {
			return listaAdNetTrUseEqRo;
		}

		/**
		 * @param listaAdNetTrUseEqRo
		 *            the listaAdNetTrUseEqRo to set
		 */
		public void setListaAdNetTrUseEqRo(List<RcsorAdNetTrUseEqRo> listaAdNetTrUseEqRo) {
			this.listaAdNetTrUseEqRo = listaAdNetTrUseEqRo;
		}

		/**
		 * @return the listaAdTrailSpULrSp
		 */
		public List<RcsorAdTrailSpULrSp> getListaAdTrailSpULrSp() {
			return listaAdTrailSpULrSp;
		}

		/**
		 * @param listaAdTrailSpULrSp
		 *            the listaAdTrailSpULrSp to set
		 */
		public void setListaAdTrailSpULrSp(List<RcsorAdTrailSpULrSp> listaAdTrailSpULrSp) {
			this.listaAdTrailSpULrSp = listaAdTrailSpULrSp;
		}

		/**
		 * @return the listaAdapEquipRoleSp
		 */
		public List<RcsopAdapEquipRoleSp> getListaAdapEquipRoleSp() {
			return listaAdapEquipRoleSp;
		}

		/**
		 * @param listaAdapEquipRoleSp
		 *            the listaAdapEquipRoleSp to set
		 */
		public void setListaAdapEquipRoleSp(List<RcsopAdapEquipRoleSp> listaAdapEquipRoleSp) {
			this.listaAdapEquipRoleSp = listaAdapEquipRoleSp;
		}

		/**
		 * @return the listaAdEqRoRelIsTriBy
		 */
		public List<RcsorAdEqRoRelIsTriBy> getListaAdEqRoRelIsTriBy() {
			return listaAdEqRoRelIsTriBy;
		}

		/**
		 * @param listaAdEqRoRelIsTriBy
		 *            the listaAdEqRoRelIsTriBy to set
		 */
		public void setListaAdEqRoRelIsTriBy(List<RcsorAdEqRoRelIsTriBy> listaAdEqRoRelIsTriBy) {
			this.listaAdEqRoRelIsTriBy = listaAdEqRoRelIsTriBy;
		}

		/**
		 * @return the listaAdEqRoUseFacSp
		 */
		public List<RcsorAdEqRoUseFacSp> getListaAdEqRoUseFacSp() {
			return listaAdEqRoUseFacSp;
		}

		/**
		 * @param listaAdEqRoUseFacSp
		 *            the listaAdEqRoUseFacSp to set
		 */
		public void setListaAdEqRoUseFacSp(List<RcsorAdEqRoUseFacSp> listaAdEqRoUseFacSp) {
			this.listaAdEqRoUseFacSp = listaAdEqRoUseFacSp;
		}

		/**
		 * @return the listaNeLayHEqRole
		 */
		public List<RcsorNeLayHEqRole> getListaNeLayHEqRole() {
			return listaNeLayHEqRole;
		}

		/**
		 * @param listaNeLayHEqRole
		 *            the listaNeLayHEqRole to set
		 */
		public void setListaNeLayHEqRole(List<RcsorNeLayHEqRole> listaNeLayHEqRole) {
			this.listaNeLayHEqRole = listaNeLayHEqRole;
		}

		/**
		 * @return the listaAdapParameter
		 */
		public List<RcsopAdapParameter> getListaAdapParameter() {
			return listaAdapParameter;
		}

		/**
		 * @param listaAdapParameter
		 *            the listaAdapParameter to set
		 */
		public void setListaAdapParameter(List<RcsopAdapParameter> listaAdapParameter) {
			this.listaAdapParameter = listaAdapParameter;
		}

		/**
		 * @return the listaNetLayUseAc
		 */
		public List<RcsorNetLayUseAc> getListaNetLayUseAc() {
			return listaNetLayUseAc;
		}

		/**
		 * @param listaNetLayUseAc
		 *            the listaNetLayUseAc to set
		 */
		public void setListaNetLayUseAc(List<RcsorNetLayUseAc> listaNetLayUseAc) {
			this.listaNetLayUseAc = listaNetLayUseAc;
		}

		/**
		 * @return the listaNetLayerHasFac
		 */
		public List<RcsorNetLayerHasFac> getListaNetLayerHasFac() {
			return listaNetLayerHasFac;
		}

		/**
		 * @param listaNetLayerHasFac
		 *            the listaNetLayerHasFac to set
		 */
		public void setListaNetLayerHasFac(List<RcsorNetLayerHasFac> listaNetLayerHasFac) {
			this.listaNetLayerHasFac = listaNetLayerHasFac;
		}

		/**
		 * @return the listaNetLayerHTrail
		 */
		public List<RcsorNetLayerHTrail> getListaNetLayerHTrail() {
			return listaNetLayerHTrail;
		}

		/**
		 * @param listaNetLayerHTrail
		 *            the listaNetLayerHTrail to set
		 */
		public void setListaNetLayerHTrail(List<RcsorNetLayerHTrail> listaNetLayerHTrail) {
			this.listaNetLayerHTrail = listaNetLayerHTrail;
		}

		/**
		 * @return the listaNeLayerHNetServ
		 */
		public List<RcsorNeLayerHNetServ> getListaNeLayerHNetServ() {
			return listaNeLayerHNetServ;
		}

		/**
		 * @param listaNeLayerHNetServ
		 *            the listaNeLayerHNetServ to set
		 */
		public void setListaNeLayerHNetServ(List<RcsorNeLayerHNetServ> listaNeLayerHNetServ) {
			this.listaNeLayerHNetServ = listaNeLayerHNetServ;
		}

		public List<RcsorAdNSeSpUseT> getListarcsorAdNSeSpUseT() {
			return listaRcsorAdNSeSpUseT;
		}

		public void setListarcsorAdNSeSpUseT(List<RcsorAdNSeSpUseT> listaRcsorAdNSeSpUseT) {
			this.listaRcsorAdNSeSpUseT = listaRcsorAdNSeSpUseT;
		}

	}

	/**
	 * Servicio que recibe como entrada un IdResuestMN y devuelve todos los
	 * datos de la entidad AdapXML y sus entidades relacionadas.
	 * 
	 * @param getadapxml_in
	 * @param te_Cabecera
	 * @param te_Metadatos
	 * @return
	 * @throws TE_Excepcion
	 */
	public GetAdapXML_OUT getAdapXML(GetAdapXML_IN getadapxml_in, TE_Cabecera te_Cabecera, TE_Metadatos te_Metadatos)
			throws TE_Excepcion {
		GetAdapXML_OUT out = new GetAdapXML_OUT();

		try {
			Long idRequest = getadapxml_in.getAdapXML().getAdapIdRequestMN();
			com.telefonica.gdre.dao.adaptationresource.dto.GetAdapXMLDaoOut adapXml = rcsopAdapXmlRepository
					.getAdapXML(idRequest);
			if (adapXml == null) {
				throw new TE_Excepcion(ERROR_000102, DESCRIPTION_000102);
			} else {
				Long id = adapXml.getAxmlIdAdapXML();
				RcsopAdapXml adap = new RcsopAdapXml();
				adap = rcsopAdapXmlRepository.getOne(id);
				// seteamos el objeto de salida con los atributos del objeto
				// adap
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.AdapXML_DTO_OUT adapOut = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.AdapXML_DTO_OUT();
				adapOut.setXml(adap.getAxmlDoXml());
				adapOut.setXmlDate(adap.getAxmlTiXmlDate());
				adapOut.setAdapIdRequest(new BigDecimal(getadapxml_in.getAdapXML().getAdapIdRequestMN()));
				adapOut.setId(adap.getAxmlIdAdapXml());
				AdapXMLType_DTO_OUT adapXT = new AdapXMLType_DTO_OUT();
				adapXT.setId(adap.getRcsodAdapXmlType().getAxmtIdAdapXmlType());
				adapOut.setAdapXMLType(adapXT);
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.AdapNetworkLayerSpec_DTO_OUT adapNLS = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.AdapNetworkLayerSpec_DTO_OUT();
				adapNLS.setAdapCode(adap.getRcsopAdNetLayerSp().getAnlsIdAdapCode());
				adapOut.setAdapNetworkLayerSpec(adapNLS);
				OrchestrationPlanTask_DTO_OUT adaOrch = new OrchestrationPlanTask_DTO_OUT();
				adaOrch.setId(adap.getOpelIdOrchPlanElement());
				adapOut.setOrchestrationPlanTask(adaOrch);
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.AdapOperation_DTO_OUT adaOp = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getadapxml.AdapOperation_DTO_OUT();
				adaOp.setAdapCode(adap.getRcsopAdapOperation().getAdopIdAdapCode());
				adapOut.setAdapOperation(adaOp);
				out.setAdapXML(adapOut);
			}
		} catch (PersistenceException e) {
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}

		return out;
	}

	/**
	 * Servicio que recibe como entrada un adapCode de la adapNetworkLayerSpec y
	 * se actualiza ese campo junto description, adapCode y value de
	 * adapParameters
	 * 
	 * @param getwholeadaptationmodelbyadapnetworklayerspec_in
	 * @param te_Cabecera
	 * @param te_Metadatos
	 * @return
	 * @throws TE_Excepcion
	 */
	@Transactional
	public GetWholeAdaptationModelByAdapNetworkLayerSpec_OUT getWholeAdaptationModelByAdapNetworkLayerSpec(
			GetWholeAdaptationModelByAdapNetworkLayerSpec_IN getwholeadaptationmodelbyadapnetworklayerspec_in,
			TE_Cabecera te_Cabecera, TE_Metadatos te_Metadatos) throws TE_Excepcion {
		Map<String, Map<String, List<?>>> mapaDatosRcsopAdNetLayerSp = null;

		GetWholeAdaptationModelByAdapNetworkLayerSpec_OUT out = new GetWholeAdaptationModelByAdapNetworkLayerSpec_OUT();

		LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Paso 1 FindAll");
		ListasFindAll listas = new ListasFindAll();
		LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Paso 1 FindAll");
		LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Paso 2 ConstruirMapas");
		MapasCache mapasCache = new MapasCache(listas);
		LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Paso 2 ConstruirMapas");
		LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Paso 3 ConstruirSalida");

		List<RcsopAdNetLayerSp> listaRcsopAdNetLayerSp = new ArrayList<RcsopAdNetLayerSp>();

		/**
		 * Informado
		 * getwholeadaptationmodelbyadapnetworklayerspec_in.adapNetworkLayerSpec
		 */
		if (getwholeadaptationmodelbyadapnetworklayerspec_in.getAdapNetworkLayerSpec() != null) {
			String adapCodeIn = getwholeadaptationmodelbyadapnetworklayerspec_in.getAdapNetworkLayerSpec()
					.getAdapCode();
			if (!mapasCache.getMapRcsopAdNetLayerSp().containsKey(adapCodeIn)) {
				LOGGER.error("AdapNetworkLayerSpec informado a la entrada inexistente en BBDD: " + adapCodeIn);
				throw new TE_Excepcion(ERROR_000102, DESCRIPTION_000102);
			}
			listaRcsopAdNetLayerSp.add(mapasCache.getMapRcsopAdNetLayerSp().get(adapCodeIn));
		} else {
			listaRcsopAdNetLayerSp.addAll(mapasCache.getMapRcsopAdNetLayerSp().values());
			bloqueAdapParameter(out, mapasCache);
		}

		// Iteramos sobre los adNetLayerSp
		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT[] adapNetworkLayerSpecs = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT[mapasCache
				.getMapRcsopAdNetLayerSp().keySet().size()];
		int contAdapNetworkLayerSpecs = 0;
		for (RcsopAdNetLayerSp rcsopAdNetLayerSp : listaRcsopAdNetLayerSp) {
			String rcsopAdNetLayerSpCode = rcsopAdNetLayerSp.getAnlsIdAdapCode();
			// Seteo el adapNetworkLayerSpec
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT();
			adapNetworkLayerSpec_DTO_OUT.setAdapCode(rcsopAdNetLayerSpCode);
			adapNetworkLayerSpec_DTO_OUT.setApplicationId(rcsopAdNetLayerSp.getGenaIdApplication());

			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Llamando Bloque 1 para código "
					+ rcsopAdNetLayerSpCode);
			// Bloque 1
			creaBloque1(mapasCache, rcsopAdNetLayerSpCode, adapNetworkLayerSpec_DTO_OUT);
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Llamando Bloque 1 para código "
					+ rcsopAdNetLayerSpCode);

			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Llamando Bloque 2 para código "
					+ rcsopAdNetLayerSpCode);
			// Bloque 2
			creaBloque2(mapasCache, rcsopAdNetLayerSpCode, adapNetworkLayerSpec_DTO_OUT);
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Llamando Bloque 2 para código "
					+ rcsopAdNetLayerSpCode);

			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Llamando Bloque 3 para código "
					+ rcsopAdNetLayerSpCode);
			// Bloque 3
			creaBloque3(mapasCache, rcsopAdNetLayerSpCode, adapNetworkLayerSpec_DTO_OUT);
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Llamando Bloque 3 para código "
					+ rcsopAdNetLayerSpCode);

			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Llamando Bloque 4 para código "
					+ rcsopAdNetLayerSpCode);
			// Bloque 4
			creaBloque4(mapasCache, rcsopAdNetLayerSpCode, adapNetworkLayerSpec_DTO_OUT);
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Llamando Bloque 4 para código "
					+ rcsopAdNetLayerSpCode);

			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Llamando Bloque 5 para código "
					+ rcsopAdNetLayerSpCode);
			// Bloque 5
			creaBloque5(mapasCache, rcsopAdNetLayerSpCode, adapNetworkLayerSpec_DTO_OUT);
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Llamando Bloque 5 para código "
					+ rcsopAdNetLayerSpCode);

			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Llamando Bloque 6 para código "
					+ rcsopAdNetLayerSpCode);
			// Bloque 6
			creaBloque6(mapasCache, rcsopAdNetLayerSpCode, adapNetworkLayerSpec_DTO_OUT);
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Llamando Bloque 6 para código "
					+ rcsopAdNetLayerSpCode);

			// Block 7
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - INICIO Llamando Bloque 7 para código "
					+ rcsopAdNetLayerSpCode);
			creaBloque7(mapasCache, rcsopAdNetLayerSpCode, adapNetworkLayerSpec_DTO_OUT);
			LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Llamando Bloque 7 para código "
					+ rcsopAdNetLayerSpCode);

			adapNetworkLayerSpecs[contAdapNetworkLayerSpecs] = adapNetworkLayerSpec_DTO_OUT;
			contAdapNetworkLayerSpecs++;
		}
		out.setAdapNetworkLayerSpecs(adapNetworkLayerSpecs);
		// DAT - FIN

		LOGGER.info("getWholeAdaptationModelByAdapNetworkLayerSpec - FIN Paso 3 ConstruirSalida");
		return out;
	}

	private void bloqueAdapParameter(GetWholeAdaptationModelByAdapNetworkLayerSpec_OUT out, MapasCache mapasCache) {
		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapParameters_DTO_OUT[] adapParameters = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapParameters_DTO_OUT[mapasCache
				.getMapaAdapParameter().keySet().size()];
		int contIndexParameter = 0;
		for (Entry<String, RcsopAdapParameter> par : mapasCache.getMapaAdapParameter().entrySet()) {
			RcsopAdapParameter rcsopAdapParameter = par.getValue();
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapParameters_DTO_OUT adapParameter = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapParameters_DTO_OUT();
			adapParameter.setAdapCode(rcsopAdapParameter.getAdpaIdAdapCode());
			adapParameter.setValue(rcsopAdapParameter.getAdpaNaValue());
			adapParameter.setDescription(rcsopAdapParameter.getAdpaDsAdapParameters());
			adapParameters[contIndexParameter] = adapParameter;
			contIndexParameter++;
		}
		out.setAdapParameters(adapParameters);
	}

	private void creaBloque1(MapasCache mapasCache, String rcsopAdNetLayerSpCode,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT) {
		// Bloque 1
		// Recorro los RcsorAdNetLaySpExeOp
		if (mapasCache.getMapaAdNetLaySpExeOp().containsKey(rcsopAdNetLayerSpCode)) {
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetLaySpExecutesOp_DTO_OUT[] arrayAdNetLaySpExeOps = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetLaySpExecutesOp_DTO_OUT[mapasCache
					.getMapaAdNetLaySpExeOp().get(rcsopAdNetLayerSpCode).size()];
			int contArrayAdNetLaySpExeOps = 0;
			for (RcsorAdNetLaySpExeOp rcsorAdNetLaySpExeOpMap : mapasCache.getMapaAdNetLaySpExeOp()
					.get(rcsopAdNetLayerSpCode)) {

				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetLaySpExecutesOp_DTO_OUT adNetLaySpExeOp = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetLaySpExecutesOp_DTO_OUT();
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapOperation_DTO_OUT adapOperation = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapOperation_DTO_OUT();
				LOGGER.info("INI - lista operation + operationType");
				adapOperation.setAdapCode(rcsorAdNetLaySpExeOpMap.getRcsopAdapOperation().getAdopIdAdapCode());
				adapOperation.setId(rcsorAdNetLaySpExeOpMap.getRcsopAdapOperation().getAdopIdAdapOperation());
				adapOperation.setAdapOperationTypeName(rcsorAdNetLaySpExeOpMap.getRcsopAdapOperation()
						.getRcsodAdapOperationType().getAoptNaAdapOperationType());
				adapOperation.setAdapOperationTypeId(rcsorAdNetLaySpExeOpMap.getRcsopAdapOperation()
						.getRcsodAdapOperationType().getAoptIdAdapOperationType());

				LOGGER.info("INI - lista listaRcsoaAdOpTran");
				if (mapasCache.getMapaaAdOpTran().containsKey(adapOperation.getId())) {
					Long[] arrayItemWorkFlowSpecTaskIds = new Long[mapasCache.getMapaaAdOpTran()
							.get(adapOperation.getId()).size()];
					int contArrayItemWorkFlowSpecTaskIds = 0;
					for (RcsoaAdOpTran opTran : mapasCache.getMapaaAdOpTran().get(adapOperation.getId())) {
						arrayItemWorkFlowSpecTaskIds[contArrayItemWorkFlowSpecTaskIds] = opTran.getId()
								.getIwstIdItemWflowSpTask();
						contArrayItemWorkFlowSpecTaskIds++;
					}
					adapOperation.setItemWorkflowSpecTaskIds(arrayItemWorkFlowSpecTaskIds);
				}
				LOGGER.info("FIN  - lista listaRcsoaAdOpTran");

				adNetLaySpExeOp.setAdapOperation(adapOperation);
				LOGGER.info("FIN  - lista operation + operationType");

				LOGGER.info("INI  - lista lockVoiceType");
				if (rcsorAdNetLaySpExeOpMap.getRcsodLockVoiceType() != null) {
					adNetLaySpExeOp.setLockVoiceTypeId(
							rcsorAdNetLaySpExeOpMap.getRcsodLockVoiceType().getLvotIdLockVoiceType());
					adNetLaySpExeOp.setLockVoiceTypeName(
							rcsorAdNetLaySpExeOpMap.getRcsodLockVoiceType().getLvotNaLockVoiceType());
				}
				LOGGER.info("FIN  - lista lockVoiceType");

				arrayAdNetLaySpExeOps[contArrayAdNetLaySpExeOps] = adNetLaySpExeOp;
				contArrayAdNetLaySpExeOps++;
			}

			adapNetworkLayerSpec_DTO_OUT.setAdapNetLaySpExecutesOps(arrayAdNetLaySpExeOps);
		}
	}

	private void creaBloque2(MapasCache mapasCache, String rcsopAdNetLayerSpCode,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT) {

	

		if (mapasCache.getMapaAdSerLiUseAcSp().get(rcsopAdNetLayerSpCode) != null) {
	
			AdapServiceLineSpec_DTO_OUT[] arrayAdapServiceLineSpec = new AdapServiceLineSpec_DTO_OUT[mapasCache
				.getMapaAdSerLiUseAcSp().get(rcsopAdNetLayerSpCode).size()];
			
			int contArrayAdapServiceLineSpec = 0;

			for (RcsorAdSerLiUseAcSp rcsorAdSerLiUseAcSp : mapasCache.getMapaAdSerLiUseAcSp().get(rcsopAdNetLayerSpCode)) {
				AdapServiceLineSpec_DTO_OUT adapServiceLineSpec = creaAdapServiceLineSpec(mapasCache,
						rcsorAdSerLiUseAcSp.getRcsopAdapSerLineSp().getAslsIdAdapCode(), rcsorAdSerLiUseAcSp.getRcsopAdapSerLineSp().getAslsNaMnemonic());
		
				arrayAdapServiceLineSpec[contArrayAdapServiceLineSpec] = adapServiceLineSpec;
				contArrayAdapServiceLineSpec++;
			}
			adapNetworkLayerSpec_DTO_OUT.setAdapServiceLineSpecs(arrayAdapServiceLineSpec);
		}
		
	}

	private AdapServiceLineSpec_DTO_OUT creaAdapServiceLineSpec(MapasCache mapasCache, String rcsopAdNetLayerSpCode, String mnemonic) {
		AdapServiceLineSpec_DTO_OUT adapServiceLineSpec = new AdapServiceLineSpec_DTO_OUT();
		adapServiceLineSpec.setAdapCode(rcsopAdNetLayerSpCode);
		adapServiceLineSpec.setMnemonic(mnemonic);
		// ######################################################################################################################
		// Setting AdapSerLineUseAccessSp
		// ######################################################################################################################

		
		if (mapasCache.getMapaAdSerLiUseAcSp().get(rcsopAdNetLayerSpCode) != null) {
			AdapSerLineUseAccessSp_DTO_OUT[] arrayAdapSerLineUseAccessSp = new AdapSerLineUseAccessSp_DTO_OUT[mapasCache
					.getMapaAdSerLiUseAcSp().get(rcsopAdNetLayerSpCode).size()];
			int contarrayAdapSerLineUseAccessSp = 0;

			for (RcsorAdSerLiUseAcSp rcsorAdAccUseEqRoSp : mapasCache.getMapaAdSerLiUseAcSp()
					.get(rcsopAdNetLayerSpCode)) {
				AdapSerLineUseAccessSp_DTO_OUT adapSerLineUseAccessSp = new AdapSerLineUseAccessSp_DTO_OUT();

				AdapAccessSpec_DTO_OUT adapAccessSpec = creaAdapAccesSpec(mapasCache,
						rcsorAdAccUseEqRoSp.getRcsopAdapAccSpec().getAacsIdAdapCode(), rcsorAdAccUseEqRoSp.getRcsopAdapAccSpec().getAacsNaMnemonic());
				adapSerLineUseAccessSp.setAdapAccessSpec(adapAccessSpec);
				adapSerLineUseAccessSp.setMaxCardinality(rcsorAdAccUseEqRoSp.getSluaQuMaxCardinality());
				adapSerLineUseAccessSp.setMinCardinality(rcsorAdAccUseEqRoSp.getSluaQuMinCardinality());

				arrayAdapSerLineUseAccessSp[contarrayAdapSerLineUseAccessSp] = adapSerLineUseAccessSp;
				contarrayAdapSerLineUseAccessSp++;
			}
			adapServiceLineSpec.setAdapSerLineUseAccessSps(arrayAdapSerLineUseAccessSp);
		}

		// ######################################################################################################################
		// Setting AdapServLineSpUseNetServ
		// ######################################################################################################################

		if (mapasCache.getMapaAdSeliSpUseNetse().get(rcsopAdNetLayerSpCode) != null) {
			AdapServLineSpUseNetServ_DTO_OUT[] arrayAdapServLineSpUseNetServ = new AdapServLineSpUseNetServ_DTO_OUT[mapasCache
					.getMapaAdSeliSpUseNetse().get(rcsopAdNetLayerSpCode).size()];
			int contarrayAdapServLineSpUseNetServ = 0;

			for (RcsorAdSeliSpUseNetse rcsorAdSeliSpUseNetse : mapasCache.getMapaAdSeliSpUseNetse()
					.get(rcsopAdNetLayerSpCode)) {
				AdapServLineSpUseNetServ_DTO_OUT adapServLineSpUseNetServ = new AdapServLineSpUseNetServ_DTO_OUT();

				AdapNetworkServiceSpec_DTO_OUT adapNetworkServiceSpec = creaAdapNetworkServiceSpecDTO(mapasCache,
						rcsorAdSeliSpUseNetse.getRcsopAdapNetSerSp().getAnssIdAdapCode());
				adapServLineSpUseNetServ.setAdapNetworkServiceSpec(adapNetworkServiceSpec);
				adapServLineSpUseNetServ.setMaxCardinality(rcsorAdSeliSpUseNetse.getAsnuQuMaxCardinality());
				adapServLineSpUseNetServ.setMinCardinality(rcsorAdSeliSpUseNetse.getAsnuQuMinCardinality());

				arrayAdapServLineSpUseNetServ[contarrayAdapServLineSpUseNetServ] = adapServLineSpUseNetServ;
				contarrayAdapServLineSpUseNetServ++;
			}

			adapServiceLineSpec.setAdapServLineSpUseNetServs(arrayAdapServLineSpUseNetServ);
		}

		// ######################################################################################################################
		// Setting AdapSLSpUseFacilitySp
		// ######################################################################################################################

		if (mapasCache.getMapaAdSlSpUseFaSp().get(rcsopAdNetLayerSpCode) != null) {
			AdapSLSpUseFacilitySp_DTO_OUT[] arrayAdapSLSpUseFacilitySp = new AdapSLSpUseFacilitySp_DTO_OUT[mapasCache
					.getMapaAdSlSpUseFaSp().get(rcsopAdNetLayerSpCode).size()];
			int contarrayAdapSLSpUseFacilitySp = 0;

			for (RcsorAdSlSpUseFaSp rcsorAdSlSpUseFaSp : mapasCache.getMapaAdSlSpUseFaSp().get(rcsopAdNetLayerSpCode)) {
				AdapSLSpUseFacilitySp_DTO_OUT adapSLSpUseFacilitySp = new AdapSLSpUseFacilitySp_DTO_OUT();

				AdapFacilitySpec_DTO_OUT adapFacilitySpec = creaAdapFacilitySpec(mapasCache,
						rcsorAdSlSpUseFaSp.getRcsopAdapFacSpec().getAfspIdAdapCode());
				adapSLSpUseFacilitySp.setAdapFacilitySpec(adapFacilitySpec);
				adapSLSpUseFacilitySp.setMaxCardinality(rcsorAdSlSpUseFaSp.getAslfQuMaxCardinality());
				adapSLSpUseFacilitySp.setMinCardinality(rcsorAdSlSpUseFaSp.getAslfQuMinCardinality());

				arrayAdapSLSpUseFacilitySp[contarrayAdapSLSpUseFacilitySp] = adapSLSpUseFacilitySp;
				contarrayAdapSLSpUseFacilitySp++;
			}

			adapServiceLineSpec.setAdapSLSpUseFacilitySps(arrayAdapSLSpUseFacilitySp);
		}

		// ######################################################################################################################
		// Setting ResourceFacingServiceSpecAtomicId
		// ######################################################################################################################

		if (mapasCache.getMapaAdapSeLiTrRfsSp().get(rcsopAdNetLayerSpCode) != null) {
			Long[] arrayResourceFacingServiceSpecAtomicId = new Long[mapasCache.getMapaAdapSeLiTrRfsSp().get(rcsopAdNetLayerSpCode).size()];
			int contArrayResourceFacingServiceSpecAtomicId =0;
			
			for(RcsorAdapSeLiTrRfsSp rcsorAdapSeLiTrRfsSp : mapasCache.getMapaAdapSeLiTrRfsSp().get(rcsopAdNetLayerSpCode)){
				Long resourceFacingServiceSpecAtomicId = rcsorAdapSeLiTrRfsSp.getId().getSespIdServSpec();
				
				arrayResourceFacingServiceSpecAtomicId[contArrayResourceFacingServiceSpecAtomicId]=resourceFacingServiceSpecAtomicId;
				contArrayResourceFacingServiceSpecAtomicId++;
			}
			adapServiceLineSpec.setResourceFacingServiceSpecAtomicIds(arrayResourceFacingServiceSpecAtomicId);
			
		}


		return adapServiceLineSpec;
	}

	/**
	 * @param mapasCache
	 * @param rcsopAdNetLayerSpCode
	 * @param adapNetworkLayerSpec_DTO_OUT
	 */
	private void creaBloque3(MapasCache mapasCache, String rcsopAdNetLayerSpCode,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT) {
		LOGGER.info("INI - listaRcsopAdapNetSerSp ");
		if (mapasCache.getMapaNeLayerHNetServ().get(rcsopAdNetLayerSpCode) != null) {
			AdapNetworkServiceSpec_DTO_OUT[] arrayAdapNetworkServiceSpecs = new AdapNetworkServiceSpec_DTO_OUT[mapasCache
					.getMapaNeLayerHNetServ().get(rcsopAdNetLayerSpCode).size()];
			int contArrayAdapNetworkServiceSpecs = 0;
			for (RcsopAdapNetSerSp rcsopAdapNetSerSp : mapasCache.getMapaNeLayerHNetServ().get(rcsopAdNetLayerSpCode)) {
				AdapNetworkServiceSpec_DTO_OUT adapNetworkServiceSpecs = creaAdapNetworkServiceSpecDTO(mapasCache,
						rcsopAdapNetSerSp.getAnssIdAdapCode());

				arrayAdapNetworkServiceSpecs[contArrayAdapNetworkServiceSpecs] = adapNetworkServiceSpecs;
				contArrayAdapNetworkServiceSpecs++;
			}

			adapNetworkLayerSpec_DTO_OUT.setAdapNetworkServiceSpecs(arrayAdapNetworkServiceSpecs);
		}
		LOGGER.info("FIN - listaRcsopAdapNetSerSp ");
	}

	/**
	 * @param mapasCache
	 * @param rcsopAdNetLayerSpCode
	 * @param adapNetworkLayerSpec_DTO_OUT
	 */
	private void creaBloque4(MapasCache mapasCache, String rcsopAdNetLayerSpCode,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT) {

		

		if (mapasCache.getMapaNetLayUseAc().get(rcsopAdNetLayerSpCode) != null) {
			AdapAccessSpec_DTO_OUT[] arrayAdapAccessSpec = new AdapAccessSpec_DTO_OUT[mapasCache.getMapaNetLayUseAc()
			                                                                          .get(rcsopAdNetLayerSpCode).size()];

			int contArrayAdapAccessSpec = 0;

			for (RcsorNetLayUseAc rcsorNetLayUseAc : mapasCache.getMapaNetLayUseAc().get(rcsopAdNetLayerSpCode)) {
				AdapAccessSpec_DTO_OUT adapAccessSpec = creaAdapAccesSpec(mapasCache, rcsorNetLayUseAc.getId().getAacsIdAdapCode(), rcsorNetLayUseAc.getRcsopAdapAccSpec().getAacsNaMnemonic());
				
				arrayAdapAccessSpec[contArrayAdapAccessSpec] = adapAccessSpec;
				contArrayAdapAccessSpec++;
			}
			adapNetworkLayerSpec_DTO_OUT.setAdapAccessSpecs(arrayAdapAccessSpec);
		}
	}

	private AdapAccessSpec_DTO_OUT creaAdapAccesSpec(MapasCache mapasCache, String rcsopAdNetLayerSpCode, String mnemonic) {

		AdapAccessSpec_DTO_OUT adapAccessSpec = new AdapAccessSpec_DTO_OUT();
		adapAccessSpec.setAdapCode(rcsopAdNetLayerSpCode);
		adapAccessSpec.setMnemonic(mnemonic);
		
		// ######################################################################################################################
		// Setting AdapAccessUseEquipRoleSps
		// ######################################################################################################################

		if (mapasCache.getMapaAdAccUseEqRoSp().get(rcsopAdNetLayerSpCode) != null) {
			AdapAccessUseEquipRoleSp_DTO_OUT[] arrayAdapAccessUseEquipRoleSp = new AdapAccessUseEquipRoleSp_DTO_OUT[mapasCache
					.getMapaAdAccUseEqRoSp().get(rcsopAdNetLayerSpCode).size()];
			int contArrayAdapAccessUseEquipRoleSp = 0;

			for (RcsorAdAccUseEqRoSp rcsorAdAccUseEqRoSp : mapasCache.getMapaAdAccUseEqRoSp()
					.get(rcsopAdNetLayerSpCode)) {
				AdapAccessUseEquipRoleSp_DTO_OUT adapAccessUseEquipRoleSp = new AdapAccessUseEquipRoleSp_DTO_OUT();
				AdapEquipmentRoleSpec_DTO_OUT adapEquipmentRoleSpec = creaAdapEquipamnentRoleSpec(mapasCache,
						rcsorAdAccUseEqRoSp.getRcsopAdapEquipRoleSp().getAersIdAdapCode(), rcsorAdAccUseEqRoSp.getRcsopAdapEquipRoleSp().getAersNaMnemonic());

				adapAccessUseEquipRoleSp.setAdapEquipmentRoleSpec(adapEquipmentRoleSpec);
				adapAccessUseEquipRoleSp.setMaxCardinality(rcsorAdAccUseEqRoSp.getAaerQuMaxCardinality());
				adapAccessUseEquipRoleSp.setMinCardinality(rcsorAdAccUseEqRoSp.getAaerQuMinCardinality());

				arrayAdapAccessUseEquipRoleSp[contArrayAdapAccessUseEquipRoleSp] = adapAccessUseEquipRoleSp;
				contArrayAdapAccessUseEquipRoleSp++;
			}
			adapAccessSpec.setAdapAccessUseEquipRoleSps(arrayAdapAccessUseEquipRoleSp);
		}

		// ######################################################################################################################
		// Setting AdapAccessUseFacSpecs
		// ######################################################################################################################

		if (mapasCache.getMapaAdAccUseFacSpec().get(rcsopAdNetLayerSpCode) != null) {
			AdapAccessUseFacSpec_DTO_OUT[] arrayAdapAccessUseFacSpec = new AdapAccessUseFacSpec_DTO_OUT[1];
			int counterArrayAdapAccessUseFacSpec = 0;
			for(RcsorAdAccUseFacSpec rcsorAdAccUseFacSpec : mapasCache.getMapaAdAccUseFacSpec().get(rcsopAdNetLayerSpCode)){
				AdapAccessUseFacSpec_DTO_OUT adapAccessUseFacSpec = new AdapAccessUseFacSpec_DTO_OUT();

				AdapFacilitySpec_DTO_OUT adapFacilitySpec = creaAdapFacilitySpec(mapasCache,
						rcsorAdAccUseFacSpec.getRcsopAdapFacSpec().getAfspIdAdapCode());

				adapAccessUseFacSpec.setAdapFacilitySpec(adapFacilitySpec);
				adapAccessUseFacSpec.setMaxCardinality(rcsorAdAccUseFacSpec.getAufsQuMaxCardinality());
				adapAccessUseFacSpec.setMinCardinality(rcsorAdAccUseFacSpec.getAufsQuMinCardinality());
				
				arrayAdapAccessUseFacSpec[counterArrayAdapAccessUseFacSpec] = adapAccessUseFacSpec;
				counterArrayAdapAccessUseFacSpec++;
				
			}


			adapAccessSpec.setAdapAccessUseFacSpecs(arrayAdapAccessUseFacSpec);
		}
		// ######################################################################################################################
		// Setting LogicalResourceSpecCompositeIds
		// ######################################################################################################################

		if (mapasCache.getMapaAdAccSpULogRSp().get(rcsopAdNetLayerSpCode) != null) {
			Long[] arrayLogicalResourceSpecCompositeIds = new Long[mapasCache.getMapaAdAccSpULogRSp()
					.get(rcsopAdNetLayerSpCode).size()];
			int contArrayLogicalResourceSpecCompositeIds = 0;
			for (RcsorAdAccSpULogRSp rcsorAdAccSpULogRSp : mapasCache.getMapaAdAccSpULogRSp()
					.get(rcsopAdNetLayerSpCode)) {
				Long logicalResourceSpecCompositeIds = rcsorAdAccSpULogRSp.getId().getRsspIdResourceSpec();
				arrayLogicalResourceSpecCompositeIds[contArrayLogicalResourceSpecCompositeIds] = logicalResourceSpecCompositeIds;
				contArrayLogicalResourceSpecCompositeIds++;
			}
			adapAccessSpec.setLogicalResourceSpecCompositeIds(arrayLogicalResourceSpecCompositeIds);
		}

		// ######################################################################################################################
		// Setting ResourceSpecCharValueUseIds
		// ######################################################################################################################

		if (mapasCache.getMapaAdAcSpURSpChVal().get(rcsopAdNetLayerSpCode) != null) {
			Long[] arrayResourceSpecCharValueUseIds = new Long[mapasCache.getMapaAdAcSpURSpChVal()
					.get(rcsopAdNetLayerSpCode).size()];
			int contArrayResourceSpecCharValueUseIds = 0;
			for (RcsorAdAcSpURSpChVal rcsorAdAcSpURSpChVal : mapasCache.getMapaAdAcSpURSpChVal()
					.get(rcsopAdNetLayerSpCode)) {
				Long resourceSpecCharValueUseIds = rcsorAdAcSpURSpChVal.getId().getRsvuIdResSpCharValueUse();
				arrayResourceSpecCharValueUseIds[contArrayResourceSpecCharValueUseIds] = resourceSpecCharValueUseIds;
				contArrayResourceSpecCharValueUseIds++;
			}
			adapAccessSpec.setResourceSpecCharValueUseIds(arrayResourceSpecCharValueUseIds);
		}
		
		if (null == mapasCache.getMapaAdapAccessSpec().get(adapAccessSpec.getAdapCode())){
			mapasCache.getMapaAdapAccessSpec().put(adapAccessSpec.getAdapCode(), adapAccessSpec);
		}
		
		return adapAccessSpec;

	}

	/**
	 * @param mapasCache
	 * @param rcsopAdapNetSerSp
	 * @return
	 */
	private AdapNetworkServiceSpec_DTO_OUT creaAdapNetworkServiceSpecDTO(MapasCache mapasCache, String anssIdAdapCode) {
		RcsopAdapNetSerSp rcsopAdapNetSerSp = mapasCache.getMapaAdapNetSerSp().get(anssIdAdapCode);
		AdapNetworkServiceSpec_DTO_OUT adapNetworkServiceSpecs = new AdapNetworkServiceSpec_DTO_OUT();
		adapNetworkServiceSpecs.setAdapCode(rcsopAdapNetSerSp.getAnssIdAdapCode());
		adapNetworkServiceSpecs
				.setAdapNetServTypeSpId(rcsopAdapNetSerSp.getRcsodAdNetSeTypeSp().getAndsIdAdNetSeTypeSp());
		adapNetworkServiceSpecs
				.setAdapNetServTypeSpName(rcsopAdapNetSerSp.getRcsodAdNetSeTypeSp().getAndsNaAdNetSeTypeSp());
		adapNetworkServiceSpecs.setMnemonic(rcsopAdapNetSerSp.getAnssNaMnemonic());

		LOGGER.info("INI - listaRcsorAdNSspUseFacSp ");
		if (mapasCache.getMapaAdNSspUseFacSp().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			AdapNetSSpUseFacilitySp_DTO_OUT[] arrayAdapNetSSpUseFacilitySp = new AdapNetSSpUseFacilitySp_DTO_OUT[mapasCache
					.getMapaAdNSspUseFacSp().get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayAdapNetSSpUseFacilitySp = 0;
			for (RcsorAdNSspUseFacSp rcsorAdNSspUseFacSp : mapasCache.getMapaAdNSspUseFacSp()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				AdapNetSSpUseFacilitySp_DTO_OUT adapNetSSpUseFacilitySp_DTO_OUT = new AdapNetSSpUseFacilitySp_DTO_OUT();
				adapNetSSpUseFacilitySp_DTO_OUT.setMaxCardinality(rcsorAdNSspUseFacSp.getNsufQuMaxCardinality());
				adapNetSSpUseFacilitySp_DTO_OUT.setMinCardinality(rcsorAdNSspUseFacSp.getNsufQuMinCardinality());
				adapNetSSpUseFacilitySp_DTO_OUT.setOrder(rcsorAdNSspUseFacSp.getNsufNuOrder());
				AdapFacilitySpec_DTO_OUT adapFacilitySpec = null;

				if (mapasCache.getMapaAdapFacilitySpecDTOOUT()
						.containsKey(rcsorAdNSspUseFacSp.getRcsopAdapFacSpec().getAfspIdAdapCode())) {
					adapFacilitySpec = mapasCache.getMapaAdapFacilitySpecDTOOUT()
							.get(rcsorAdNSspUseFacSp.getRcsopAdapFacSpec().getAfspIdAdapCode());
				} else {
					adapFacilitySpec = creaAdapFacilitySpec(mapasCache,
							rcsorAdNSspUseFacSp.getRcsopAdapFacSpec().getAfspIdAdapCode());
				}

				adapNetSSpUseFacilitySp_DTO_OUT.setAdapFacilitySpec(adapFacilitySpec);
				arrayAdapNetSSpUseFacilitySp[contArrayAdapNetSSpUseFacilitySp] = adapNetSSpUseFacilitySp_DTO_OUT;
				contArrayAdapNetSSpUseFacilitySp++;
			}
			adapNetworkServiceSpecs.setAdapNetSSpUseFacilitySps(arrayAdapNetSSpUseFacilitySp);
		}
		LOGGER.info("FIN - listaRcsorAdNSspUseFacSp ");

		LOGGER.info("INI - listaRcsorAdNetSSpSuby ");
		if (mapasCache.getMapaAdNetSSpSuby().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			Long[] arrayRestrictionSpecIds = new Long[mapasCache.getMapaAdNetSSpSuby()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayRestrictionSpecIds = 0;
			for (RcsorAdNetSSpSuby rcsorAdNetSSpSuby : mapasCache.getMapaAdNetSSpSuby()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				arrayRestrictionSpecIds[contArrayRestrictionSpecIds] = rcsorAdNetSSpSuby.getId()
						.getBrspIdRestrictionSpec();
				contArrayRestrictionSpecIds++;
			}
			adapNetworkServiceSpecs.setRestrictionSpecIds(arrayRestrictionSpecIds);
		}
		LOGGER.info("FIN - listaRcsorAdNetSSpSuby ");

		LOGGER.info("INI - listaRcsorAdNSerTrRfsSp ");
		if (mapasCache.getMapaAdNSerTrRfsSp().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			Long[] arrayResourceFacingServiceSpecAtomicIds = new Long[mapasCache.getMapaAdNSerTrRfsSp()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayResourceFacingServiceSpecAtomicIds = 0;
			for (RcsorAdNSerTrRfsSp rcsorAdNSerTrRfsSp : mapasCache.getMapaAdNSerTrRfsSp()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				arrayResourceFacingServiceSpecAtomicIds[contArrayResourceFacingServiceSpecAtomicIds] = rcsorAdNSerTrRfsSp
						.getId().getSespIdServSpec();
				contArrayResourceFacingServiceSpecAtomicIds++;
			}
			adapNetworkServiceSpecs.setResourceFacingServiceSpecAtomicIds(arrayResourceFacingServiceSpecAtomicIds);
		}
		LOGGER.info("FIN - listaRcsorAdNSerTrRfsSp ");

		LOGGER.info("INI - listaRcsorAdNetSSPTby ");
		if (mapasCache.getMapaAdNetSSpTby().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			Long[] arrayResourceSpecCharValueUseIds = new Long[mapasCache.getMapaAdNetSSpTby()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayResourceSpecCharValueUseIds = 0;
			for (RcsorAdNetSSPTby rcsorAdNetSSPTby : mapasCache.getMapaAdNetSSpTby()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				arrayResourceSpecCharValueUseIds[contArrayResourceSpecCharValueUseIds] = rcsorAdNetSSPTby.getId()
						.getRsvuIdResSpCharValueUse();
				contArrayResourceSpecCharValueUseIds++;
			}
			adapNetworkServiceSpecs.setResourceSpecCharValueUseIds(arrayResourceSpecCharValueUseIds);
		}
		LOGGER.info("FIN - listaRcsorAdNetSSPTby ");

		LOGGER.info("INI - listaRcsorAdNetSSpTbyRec ");
		if (mapasCache.getMapaAdNetSSpTbyRec().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			Long[] arrayResourceConfigSpecIds = new Long[mapasCache.getMapaAdNetSSpTbyRec()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayResourceConfigSpecIds = 0;
			for (RcsorAdNetSSpTbyRec rcsorAdNetSSpTbyRec : mapasCache.getMapaAdNetSSpTbyRec()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				arrayResourceConfigSpecIds[contArrayResourceConfigSpecIds] = rcsorAdNetSSpTbyRec.getId()
						.getCgspIdConfigurationSpec();
				contArrayResourceConfigSpecIds++;
			}
			adapNetworkServiceSpecs.setResourceConfigSpecIds(arrayResourceConfigSpecIds);
		}
		LOGGER.info("FIN - listaRcsorAdNetSSpTbyRec ");

		LOGGER.info("INI - listaRcsorAdapNetSeSpRel/listaRcsodAdNetSerSpReTy ");
		if (mapasCache.getMapaAdapNetSeSpRel().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			AdapNetworkServiceSpecRel_DTO_OUT[] arrayAdapNetworkServiceSpecRels = new AdapNetworkServiceSpecRel_DTO_OUT[mapasCache
					.getMapaAdapNetSeSpRel().get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayAdapNetworkServiceSpecRels = 0;
			for (RcsorAdapNetSeSpRel rcsorAdapNetSeSpRel : mapasCache.getMapaAdapNetSeSpRel()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				AdapNetworkServiceSpecRel_DTO_OUT adapNetworkServiceSpecRel_DTO_OUT = new AdapNetworkServiceSpecRel_DTO_OUT();
				if (rcsorAdapNetSeSpRel.getRcsodAdNetSerSpReTy() != null) {
					AdapNetwSerSpRelType_DTO_OUT adapNetwSerSpRelType_DTO_OUT = new AdapNetwSerSpRelType_DTO_OUT();
					adapNetwSerSpRelType_DTO_OUT
							.setId(rcsorAdapNetSeSpRel.getRcsodAdNetSerSpReTy().getNrytIdAdNetSerSpReTy());
					adapNetwSerSpRelType_DTO_OUT
							.setName(rcsorAdapNetSeSpRel.getRcsodAdNetSerSpReTy().getNrytNaAdNetSerSpReTy());
					adapNetworkServiceSpecRel_DTO_OUT.setAdapNetwSerSpRelType(adapNetwSerSpRelType_DTO_OUT);
				}
				adapNetworkServiceSpecRel_DTO_OUT.setMaxCardinality(rcsorAdapNetSeSpRel.getNssrQuMaxCardinality());
				adapNetworkServiceSpecRel_DTO_OUT.setMinCardinality(rcsorAdapNetSeSpRel.getNssrQuMinCardinality());
				adapNetworkServiceSpecRel_DTO_OUT.setOrder(rcsorAdapNetSeSpRel.getNssrNuOrder());

				AdapNetworkServiceSpec_DTO_OUT adapNetworkServiceSpec_DTO_OUT_des = null;
				if (null != mapasCache.getMapaAdapNetworkServiceSpecDTO()
						.get(rcsorAdapNetSeSpRel.getRcsopAdapNetSerSp2().getAnssIdAdapCode())) {
					adapNetworkServiceSpec_DTO_OUT_des = mapasCache.getMapaAdapNetworkServiceSpecDTO()
							.get(rcsorAdapNetSeSpRel.getRcsopAdapNetSerSp2().getAnssIdAdapCode());
				} else {
					adapNetworkServiceSpec_DTO_OUT_des = creaAdapNetworkServiceSpecDTO(mapasCache,
							rcsorAdapNetSeSpRel.getRcsopAdapNetSerSp2().getAnssIdAdapCode());
				}

				adapNetworkServiceSpecRel_DTO_OUT.setAdapNetworkServiceSpec(adapNetworkServiceSpec_DTO_OUT_des);
				arrayAdapNetworkServiceSpecRels[contArrayAdapNetworkServiceSpecRels] = adapNetworkServiceSpecRel_DTO_OUT;
				contArrayAdapNetworkServiceSpecRels++;
			}
			adapNetworkServiceSpecs.setAdapNetworkServiceSpecRels(arrayAdapNetworkServiceSpecRels);
		}
		LOGGER.info("FIN - listaRcsorAdapNetSeSpRel/listaRcsodAdNetSerSpReTy ");

		LOGGER.info("INI - listaRcsorANetSeHEquRoSp ");
		if (mapasCache.getMapaANetSeHEquRoSp().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			AdapNetServHasEquipmRoleSp_DTO_OUT[] arrayAdapNetServHasEquipmRoleSp = new AdapNetServHasEquipmRoleSp_DTO_OUT[mapasCache
					.getMapaANetSeHEquRoSp().get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayAdapNetServHasEquipmRoleSp = 0;
			for (RcsorANetSeHEquRoSp rcsorANetSeHEquRoSp : mapasCache.getMapaANetSeHEquRoSp()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				AdapNetServHasEquipmRoleSp_DTO_OUT adapNetServHasEquipmRoleSp_DTO_OUT = new AdapNetServHasEquipmRoleSp_DTO_OUT();
				adapNetServHasEquipmRoleSp_DTO_OUT.setMaxCardinality(rcsorANetSeHEquRoSp.getAhqrQuMaxCardinality());
				adapNetServHasEquipmRoleSp_DTO_OUT.setMinCardinality(rcsorANetSeHEquRoSp.getAhqrQuMinCardinality());
				AdapEquipmentRoleSpec_DTO_OUT adapEquipmentRoleSpec = null;
				if (mapasCache.getMapaAdapEquipmentRoleSpecDTO().containsKey(rcsorANetSeHEquRoSp.getRcsopAdapEquipRoleSp().getAersIdAdapCode())){
					adapEquipmentRoleSpec = mapasCache.getMapaAdapEquipmentRoleSpecDTO().get(rcsorANetSeHEquRoSp.getRcsopAdapEquipRoleSp().getAersIdAdapCode());
				}else{
					adapEquipmentRoleSpec = 
							creaAdapEquipamnentRoleSpec(mapasCache, rcsorANetSeHEquRoSp.getRcsopAdapEquipRoleSp().getAersIdAdapCode(), rcsorANetSeHEquRoSp.getRcsopAdapEquipRoleSp().getAersNaMnemonic());
				}
				adapNetServHasEquipmRoleSp_DTO_OUT.setAdapEquipmentRoleSpec(adapEquipmentRoleSpec);
				arrayAdapNetServHasEquipmRoleSp[contArrayAdapNetServHasEquipmRoleSp] = adapNetServHasEquipmRoleSp_DTO_OUT;
				contArrayAdapNetServHasEquipmRoleSp++;
			}
			adapNetworkServiceSpecs.setAdapNetServHasEquipmRoleSps(arrayAdapNetServHasEquipmRoleSp);
		}
		LOGGER.info("FIN - listaRcsorANetSeHEquRoSp ");

		LOGGER.info("INI - listaRcsoaAdTaskStepSp ");
		if (mapasCache.getMapaRcsorAdNSeSpUseT().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			AdapTaskStepSpec_DTO_OUT[] arrayAdapTaskStepSpec = new AdapTaskStepSpec_DTO_OUT[mapasCache
					.getMapaRcsorAdNSeSpUseT().get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayAdapTaskStepSpec = 0;
			for (RcsoaAdTaskStepSp rcsoaAdTaskStepSp : mapasCache.getMapaRcsorAdNSeSpUseT()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				AdapTaskStepSpec_DTO_OUT adapTaskStepSpec_DTO_OUT = new AdapTaskStepSpec_DTO_OUT();
				adapTaskStepSpec_DTO_OUT.setId(rcsoaAdTaskStepSp.getAtssIdAdTaskStepSp());
				adapTaskStepSpec_DTO_OUT.setPOTaskStepsTypeId(rcsoaAdTaskStepSp.getPtsyIdPoTaskStepsType());
				adapTaskStepSpec_DTO_OUT.setResourceFacingServiceSpecAtomicId(rcsoaAdTaskStepSp.getSespIdServSpecRfs());
				adapTaskStepSpec_DTO_OUT.setResourceRoleSpecificationId(rcsoaAdTaskStepSp.getRrosIdResourceRoleSpec());
				arrayAdapTaskStepSpec[contArrayAdapTaskStepSpec] = adapTaskStepSpec_DTO_OUT;
				contArrayAdapTaskStepSpec++;
			}
			adapNetworkServiceSpecs.setAdapTaskStepSpecs(arrayAdapTaskStepSpec);
		}
		LOGGER.info("FIN - listaRcsoaAdTaskStepSp ");

		LOGGER.info("INI - listaRcsorAdNSeReqTrailSp ");
		if (mapasCache.getMapaAdNSeReqTrailSp().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			AdapNetServRequiresTrailSp_DTO_OUT[] arrayAdapNetServRequiresTrailSp = new AdapNetServRequiresTrailSp_DTO_OUT[mapasCache
					.getMapaAdNSeReqTrailSp().get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayAdapNetServRequiresTrailSp = 0;
			for (RcsorAdNSeReqTrailSp rcsorAdNSeReqTrailSp : mapasCache.getMapaAdNSeReqTrailSp()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				AdapNetServRequiresTrailSp_DTO_OUT adapNetServRequiresTrailSp_DTO_OUT = new AdapNetServRequiresTrailSp_DTO_OUT();
				adapNetServRequiresTrailSp_DTO_OUT.setMaxCardinality(rcsorAdNSeReqTrailSp.getAsrtQuMaxCardinality());
				adapNetServRequiresTrailSp_DTO_OUT.setMinCardinality(rcsorAdNSeReqTrailSp.getAsrtQuMinCardinality());
				AdapTrailSpec_DTO_OUT adapTrailSpec = null;
				if (mapasCache.getMapaAdapTrailSpecDTO()
						.containsKey(rcsorAdNSeReqTrailSp.getRcsopAdapTrailSp().getAtrrIdAdapCode())) {
					adapTrailSpec = mapasCache.getMapaAdapTrailSpecDTO()
							.get(rcsorAdNSeReqTrailSp.getRcsopAdapTrailSp().getAtrrIdAdapCode());
				} else {
					adapTrailSpec = creaAdapTrailSpecs(mapasCache,
							rcsorAdNSeReqTrailSp.getRcsopAdapTrailSp().getAtrrIdAdapCode());
				}
				adapNetServRequiresTrailSp_DTO_OUT.setAdapTrailSpec(adapTrailSpec);
				arrayAdapNetServRequiresTrailSp[contArrayAdapNetServRequiresTrailSp] = adapNetServRequiresTrailSp_DTO_OUT;
				contArrayAdapNetServRequiresTrailSp++;
			}
			adapNetworkServiceSpecs.setAdapNetServRequiresTrailSps(arrayAdapNetServRequiresTrailSp);
		}
		LOGGER.info("FIN - listaRcsorAdNSeReqTrailSp ");

		LOGGER.info("INI - listaRcsorAdapNssIsTrigBy ");
		if (mapasCache.getMapaAdapNssIsTrigBy().get(rcsopAdapNetSerSp.getAnssIdAdapCode()) != null) {
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapOperation_DTO_OUT[] arrayAdapOperation = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapOperation_DTO_OUT[mapasCache
					.getMapaAdapNssIsTrigBy().get(rcsopAdapNetSerSp.getAnssIdAdapCode()).size()];
			int contArrayAdapOperation = 0;
			for (RcsorAdapNssIsTrigBy rcsorAdapNssIsTrigBy : mapasCache.getMapaAdapNssIsTrigBy()
					.get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapOperation_DTO_OUT adapOperation = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapOperation_DTO_OUT();

				adapOperation.setAdapCode(rcsorAdapNssIsTrigBy.getRcsopAdapOperation().getAdopIdAdapCode());
				adapOperation.setId(rcsorAdapNssIsTrigBy.getRcsopAdapOperation().getAdopIdAdapOperation());
				adapOperation.setAdapOperationTypeName(rcsorAdapNssIsTrigBy.getRcsopAdapOperation()
						.getRcsodAdapOperationType().getAoptNaAdapOperationType());
				adapOperation.setAdapOperationTypeId(rcsorAdapNssIsTrigBy.getRcsopAdapOperation()
						.getRcsodAdapOperationType().getAoptIdAdapOperationType());

				arrayAdapOperation[contArrayAdapOperation] = adapOperation;
				contArrayAdapOperation++;
			}
			adapNetworkServiceSpecs.setAdapOperations(arrayAdapOperation);
		}
		LOGGER.info("FIN - listaRcsorAdapNssIsTrigBy ");

		if (null == mapasCache.getMapaAdapNetworkServiceSpecDTO().get(rcsopAdapNetSerSp.getAnssIdAdapCode())) {
			mapasCache.getMapaAdapNetworkServiceSpecDTO().put(rcsopAdapNetSerSp.getAnssIdAdapCode(),
					adapNetworkServiceSpecs);
		}

		return adapNetworkServiceSpecs;
	}

	/**
	 * @param mapasCache
	 * @param rcsopAdNetLayerSpCode
	 * @param adapNetworkLayerSpec_DTO_OUT
	 */
	private void creaBloque5(MapasCache mapasCache, String rcsopAdNetLayerSpCode,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT) {
		// Recorro los RcsopAdapNetSerSp
		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT[] arrayAdapFacilitySpecDTOOUT = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT[0];

		if (null != mapasCache.getMapaNetLayerHasFac().get(rcsopAdNetLayerSpCode)
				&& mapasCache.getMapaNetLayerHasFac().get(rcsopAdNetLayerSpCode).size() > 0) {

			arrayAdapFacilitySpecDTOOUT = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT[mapasCache
					.getMapaNetLayerHasFac().get(rcsopAdNetLayerSpCode).size()];

			int contArrayAdapFacilitySpecDTOOUT = 0;
			for (RcsopAdapFacSpec rcsopAdapFacSpec : mapasCache.getMapaNetLayerHasFac().get(rcsopAdNetLayerSpCode)) {
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT adapFacilitySpecDTOOUT = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT();
				adapFacilitySpecDTOOUT.setAdapCode(rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT.setMnemonic(rcsopAdapFacSpec.getAfspNaMnemonic());

				// mapasCache.getMapaAdapFaSpecType().get(rcsopAdapFacSpec.getRcsodAdapFaSpecType().getAfstIdAdapFaSpecType());

				adapFacilitySpecDTOOUT
						.setAdapFacilitySpecTypeId(rcsopAdapFacSpec.getRcsodAdapFaSpecType().getAfstIdAdapFaSpecType());
				adapFacilitySpecDTOOUT.setAdapFacilitySpecTypeName(
						rcsopAdapFacSpec.getRcsodAdapFaSpecType().getAfstNaAdapFaSpecType());

				// setAdapFacilitySpecRels
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT[] arrayAdapFacilitySpecRels = creaAdapFacilitySpecRel(
						mapasCache, rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT.setAdapFacilitySpecRels(arrayAdapFacilitySpecRels);

				// AdapFacValueSpecs
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT[] arrayAdapFacValueSpecs = obtenAdapFacValueSpec(
						mapasCache, rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT.setAdapFacValueSpecs(arrayAdapFacValueSpecs);

				// EntityIdentificationSpecificationIds
				Long[] arrayEntityIdentificationSpecificationIds = creaEntityIdentificationSpecificationIds(mapasCache,
						rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT
						.setEntityIdentificationSpecificationIds(arrayEntityIdentificationSpecificationIds);

				// ProductSpecCharacteristicValueIds
				Long[] arrayProductSpecCharacteristicValueIds = creaProductSpecCharacteristicValueIds(mapasCache,
						rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT.setProductSpecCharacteristicValueIds(arrayProductSpecCharacteristicValueIds);

				// ResourceSpecCharacteristicIds
				Long[] arrayResourceSpecCharacteristicIds = creaResourceSpecCharacteristicIds(mapasCache,
						rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT.setResourceSpecCharacteristicIds(arrayResourceSpecCharacteristicIds);

				// RestrictionSpecIds
				Long[] arrayRestrictionSpecIds = creaRestrictionSpecIds(mapasCache,
						rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT.setRestrictionSpecIds(arrayRestrictionSpecIds);

				// ServiceSpecCharacteristicIds
				Long[] arrayServiceSpecCharacteristicIds = creaServiceSpecCharacteristicIds(mapasCache,
						rcsopAdapFacSpec.getAfspIdAdapCode());
				adapFacilitySpecDTOOUT.setServiceSpecCharacteristicIds(arrayServiceSpecCharacteristicIds);

				arrayAdapFacilitySpecDTOOUT[contArrayAdapFacilitySpecDTOOUT] = adapFacilitySpecDTOOUT;

				if (null == mapasCache.getMapaAdapFacilitySpecDTOOUT().get(rcsopAdapFacSpec.getAfspIdAdapCode())) {
					mapasCache.getMapaAdapFacilitySpecDTOOUT().put(rcsopAdapFacSpec.getAfspIdAdapCode(),
							adapFacilitySpecDTOOUT);
				}

				contArrayAdapFacilitySpecDTOOUT++;
			}
		}
		adapNetworkLayerSpec_DTO_OUT.setAdapFacilitySpecs(arrayAdapFacilitySpecDTOOUT);
	}

	private com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT creaAdapFacilitySpec(
			MapasCache mapasCache, String codigoRcsopAdapFacSpec) {

		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT adapFacilitySpecDTOOUT = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT();

		RcsopAdapFacSpec rcsopAdapFacSpec = mapasCache.getMapaAdapFacSpec().get(codigoRcsopAdapFacSpec);

		adapFacilitySpecDTOOUT.setAdapCode(rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setMnemonic(rcsopAdapFacSpec.getAfspNaMnemonic());

		// setAdapFacilitySpecRels
		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT[] arrayAdapFacilitySpecRels = creaAdapFacilitySpecRel(
				mapasCache, rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setAdapFacilitySpecRels(arrayAdapFacilitySpecRels);

		// AdapFacValueSpecs
		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT[] arrayAdapFacValueSpec = obtenAdapFacValueSpec(
				mapasCache, rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setAdapFacValueSpecs(arrayAdapFacValueSpec);

		// adapFacilitySpecDTOOUT.setEntityIdentificationSpecificationIds(values);
		Long[] arrayEntityIdentificationSpecificationIds = creaEntityIdentificationSpecificationIds(mapasCache,
				rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setEntityIdentificationSpecificationIds(arrayEntityIdentificationSpecificationIds);

		// adapFacilitySpecDTOOUT.setProductSpecCharacteristicValueIds(values);
		Long[] arrayProductSpecCharacteristicValueIds = creaProductSpecCharacteristicValueIds(mapasCache,
				rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setProductSpecCharacteristicValueIds(arrayProductSpecCharacteristicValueIds);

		// ResourceSpecCharacteristicIds
		Long[] arrayResourceSpecCharacteristicIds = creaResourceSpecCharacteristicIds(mapasCache,
				rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setResourceSpecCharacteristicIds(arrayResourceSpecCharacteristicIds);

		// adapFacilitySpecDTOOUT.setRestrictionSpecIds(values);
		Long[] arrayRestrictionSpecIds = creaRestrictionSpecIds(mapasCache, rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setRestrictionSpecIds(arrayRestrictionSpecIds);

		// adapFacilitySpecDTOOUT.setServiceSpecCharacteristicIds(values);
		Long[] arrayServiceSpecCharacteristicIds = creaServiceSpecCharacteristicIds(mapasCache,
				rcsopAdapFacSpec.getAfspIdAdapCode());
		adapFacilitySpecDTOOUT.setServiceSpecCharacteristicIds(arrayServiceSpecCharacteristicIds);

		if (null == mapasCache.getMapaAdapFacilitySpecDTOOUT().get(rcsopAdapFacSpec.getAfspIdAdapCode())) {
			mapasCache.getMapaAdapFacilitySpecDTOOUT().put(rcsopAdapFacSpec.getAfspIdAdapCode(),
					adapFacilitySpecDTOOUT);
		}

		return adapFacilitySpecDTOOUT;

	}

	private Long[] creaEntityIdentificationSpecificationIds(MapasCache mapasCache, String rcsopAdapFacSpec) {

		List<RcsorAdFacSpTrtoEi> listaRcsorAdFacSpTrtoEi = mapasCache.getMapaAdFacSpTrtoEi().get(rcsopAdapFacSpec);
		Long[] array = new Long[0];

		if (null != listaRcsorAdFacSpTrtoEi && listaRcsorAdFacSpTrtoEi.size() > 0) {
			array = new Long[listaRcsorAdFacSpTrtoEi.size()];
			for (int i = 0; i < listaRcsorAdFacSpTrtoEi.size(); i++) {

				array[i] = listaRcsorAdFacSpTrtoEi.get(i).getId().getReisIdResourceIdentSpec();
			}
		}

		return array;
	}

	private Long[] creaProductSpecCharacteristicValueIds(MapasCache mapasCache, String rcsopAdapFacSpec) {

		List<RcsorAdFaPsChVa> listaRcsorAdFaPsChVa = mapasCache.getMapaAdFaPsChVa().get(rcsopAdapFacSpec);
		Long[] array = new Long[0];

		if (null != listaRcsorAdFaPsChVa && listaRcsorAdFaPsChVa.size() > 0) {
			array = new Long[listaRcsorAdFaPsChVa.size()];
			for (int i = 0; i < listaRcsorAdFaPsChVa.size(); i++) {
				array[i] = listaRcsorAdFaPsChVa.get(i).getId().getPsvaIdProdSpecCharVal();
			}
		}

		return array;
	}

	private Long[] creaRestrictionSpecIds(MapasCache mapasCache, String rcsopAdapFacSpec) {

		List<RcsorAdFaSHResS> listaRcsorAdFaSHResS = mapasCache.getMapaAdFaSHResS().get(rcsopAdapFacSpec);

		Long[] array = new Long[0];

		if (null != listaRcsorAdFaSHResS && listaRcsorAdFaSHResS.size() > 0) {
			array = new Long[listaRcsorAdFaSHResS.size()];
			for (int i = 0; i < listaRcsorAdFaSHResS.size(); i++) {
				array[i] = listaRcsorAdFaSHResS.get(i).getId().getBrspIdRestrictionSpec();
			}
		}

		return array;
	}

	private Long[] creaServiceSpecCharacteristicIds(MapasCache mapasCache, String rcsopAdapFacSpec) {

		List<RcsorAdFVaSpTrtoSsc> listaRcsorAdFVaSpTrtoSsc = mapasCache.getMapaAdFVaSpTrtoSsc().get(rcsopAdapFacSpec);

		Long[] array = new Long[0];

		if (null != listaRcsorAdFVaSpTrtoSsc && listaRcsorAdFVaSpTrtoSsc.size() > 0) {
			array = new Long[listaRcsorAdFVaSpTrtoSsc.size()];
			for (int i = 0; i < listaRcsorAdFVaSpTrtoSsc.size(); i++) {

				array[i] = listaRcsorAdFVaSpTrtoSsc.get(i).getId().getAfvsIdAdFacValueSpec();
			}
		}
		return array;
	}

	private void creaBloque6(MapasCache mapasCache, String rcsopAdNetLayerSpCode,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT) {
		LOGGER.info("INI - listaRcsopAdapTrailSp ");
		if (mapasCache.getMapaNetLayerHTrail().containsKey(rcsopAdNetLayerSpCode)) {
			AdapTrailSpec_DTO_OUT[] arrayAdapTrailSpecs = new AdapTrailSpec_DTO_OUT[mapasCache.getMapaNetLayerHTrail()
					.get(rcsopAdNetLayerSpCode).size()];
			int contArrayAdapTrailSpecs = 0;
			for (RcsopAdapTrailSp rcsopAdapTrailSp : mapasCache.getMapaNetLayerHTrail().get(rcsopAdNetLayerSpCode)) {
				AdapTrailSpec_DTO_OUT adapTrailSpec_DTO_OUT = creaAdapTrailSpecs(mapasCache,
						rcsopAdapTrailSp.getAtrrIdAdapCode());

				arrayAdapTrailSpecs[contArrayAdapTrailSpecs] = adapTrailSpec_DTO_OUT;
				contArrayAdapTrailSpecs++;
			}
			adapNetworkLayerSpec_DTO_OUT.setAdapTrailSpecs(arrayAdapTrailSpecs);
		}
		LOGGER.info("FIN - listaRcsopAdapTrailSp ");

	}

	private void creaBloque7(MapasCache mapasCache, String rcsopAdNetLayerSpCode,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkLayerSpec_DTO_OUT adapNetworkLayerSpec_DTO_OUT) {
		
		if(mapasCache.getMapaAdapEquipRoleSp().get(rcsopAdNetLayerSpCode) != null){
			AdapEquipmentRoleSpec_DTO_OUT[] arrayAdapEquipmentRoleSpec = new AdapEquipmentRoleSpec_DTO_OUT[mapasCache.getMapaAdapEquipRoleSp().get(rcsopAdNetLayerSpCode).size()];
			
			for(RcsopAdapEquipRoleSp rcsopAdapEquipRoleSp : mapasCache.getMapaAdapEquipRoleSp().get(rcsopAdNetLayerSpCode)){
				AdapEquipmentRoleSpec_DTO_OUT adapEquipmentRoleSpec = creaAdapEquipamnentRoleSpec(mapasCache,
						rcsopAdapEquipRoleSp.getAersIdAdapCode(), rcsopAdapEquipRoleSp.getAersNaMnemonic());
	
				arrayAdapEquipmentRoleSpec[0] = adapEquipmentRoleSpec;
			}
			
			adapNetworkLayerSpec_DTO_OUT.setAdapEquipmentRoleSpecs(arrayAdapEquipmentRoleSpec);
		}

	}

	private AdapEquipmentRoleSpec_DTO_OUT creaAdapEquipamnentRoleSpec(MapasCache mapasCache, String aersIdAdapCode, String mnemonic) {

		AdapEquipmentRoleSpec_DTO_OUT adapEquipmentRoleSpec = new AdapEquipmentRoleSpec_DTO_OUT();
		adapEquipmentRoleSpec.setAdapCode(aersIdAdapCode);
		adapEquipmentRoleSpec.setMnemonic(mnemonic);
		// ######################################################################################################################
		// Setting sAdapEquipmentRoleSpec
		// ######################################################################################################################
		if (mapasCache.getMapaNeLayHEqRole().get(aersIdAdapCode) != null) {
			RcsorNeLayHEqRole rcsorNeLayHEqRole = mapasCache.getMapaNeLayHEqRole().get(aersIdAdapCode);
			AdapEquipmentRoleSpec_DTO_OUT adapEquipmentRoleSpec2 = creaAdapEquipamnentRoleSpec(mapasCache,
					rcsorNeLayHEqRole.getId().getAersIdAdapCode(), null);
			adapEquipmentRoleSpec.setAdapEquipmentRoleSpec(adapEquipmentRoleSpec2);
		}

		// ######################################################################################################################
		// Setting ResourceRoleSpecificationIds
		// ######################################################################################################################
		if (mapasCache.getMapaAdEqRoRelIsTriBy().get(aersIdAdapCode) != null) {
			Long[] resourceRoleSpecificationIds = new Long[mapasCache.getMapaAdEqRoRelIsTriBy().get(aersIdAdapCode)
					.size()];
			int counterResourceRoleSpecificationIds = 0;
			for (RcsorAdEqRoRelIsTriBy rcsorAdEqRoRelIsTriBy : mapasCache.getMapaAdEqRoRelIsTriBy()
					.get(aersIdAdapCode)) {
				Long resourceRoleSpecificationId = rcsorAdEqRoRelIsTriBy.getId().getRrosIdResourceRoleSpec();
				resourceRoleSpecificationIds[counterResourceRoleSpecificationIds] = resourceRoleSpecificationId;
				counterResourceRoleSpecificationIds++;
			}

			adapEquipmentRoleSpec.setResourceRoleSpecificationIds(resourceRoleSpecificationIds);
		}
		// ######################################################################################################################
		// Setting ResourceRoleSpecificationIds
		// ######################################################################################################################

		if (mapasCache.getMapaAdEqRoUseFacSp().get(aersIdAdapCode) != null) {

			AdapEquipRoleUseFacilitySp_DTO_OUT[] arrayAdapEquipRoleUseFacilitySp = new AdapEquipRoleUseFacilitySp_DTO_OUT[mapasCache.getMapaAdEqRoUseFacSp().get(aersIdAdapCode).size()];
			int counterArrayAdapEquipRoleUseFacilitySp = 0;
			for(RcsorAdEqRoUseFacSp rcsorAdEqRoUseFacSp : mapasCache.getMapaAdEqRoUseFacSp().get(aersIdAdapCode)){
				AdapEquipRoleUseFacilitySp_DTO_OUT adapEquipRoleUseFacilitySp = new AdapEquipRoleUseFacilitySp_DTO_OUT();
				adapEquipRoleUseFacilitySp.setAdapFacilitySpec(creaAdapFacilitySpec(mapasCache, rcsorAdEqRoUseFacSp.getRcsopAdapFacSpec().getAfspIdAdapCode()));
				adapEquipRoleUseFacilitySp.setMaxCardinality(rcsorAdEqRoUseFacSp.getErufQuMaxCardinality());
				adapEquipRoleUseFacilitySp.setMinCardinality(rcsorAdEqRoUseFacSp.getErufQuMinCardinality());
				arrayAdapEquipRoleUseFacilitySp[counterArrayAdapEquipRoleUseFacilitySp] = adapEquipRoleUseFacilitySp;
				counterArrayAdapEquipRoleUseFacilitySp++;
			}

			

			adapEquipmentRoleSpec.setAdapEquipRoleUseFacilitySps(arrayAdapEquipRoleUseFacilitySp);
		}
		
		if (null == mapasCache.getMapaAdapEquipmentRoleSpecDTO().get(adapEquipmentRoleSpec.getAdapCode())){
			mapasCache.getMapaAdapEquipmentRoleSpecDTO().put(adapEquipmentRoleSpec.getAdapCode(), adapEquipmentRoleSpec);
		}

		return adapEquipmentRoleSpec;
	}

	/**
	 * @param mapasCache
	 * @param rcsopAdapTrailSp
	 * @return
	 */
	private AdapTrailSpec_DTO_OUT creaAdapTrailSpecs(MapasCache mapasCache, String atrrIdAdapCode) {
		RcsopAdapTrailSp rcsopAdapTrailSp = mapasCache.getMapaAdapTrailSp().get(atrrIdAdapCode);
		AdapTrailSpec_DTO_OUT adapTrailSpec_DTO_OUT = new AdapTrailSpec_DTO_OUT();
		adapTrailSpec_DTO_OUT.setAdapCode(rcsopAdapTrailSp.getAtrrIdAdapCode());
		adapTrailSpec_DTO_OUT.setMnemonic(rcsopAdapTrailSp.getAtrrNaMnemonic());

		LOGGER.info("INI - listaRcsopAdapNetTrailSp ");
		if (mapasCache.getMapaAdapNetTrailSp().get(rcsopAdapTrailSp.getAtrrIdAdapCode()) != null) {
			AdapNetTrailSpec_DTO_OUT[] arrayAdapNetTrailSpecs = new AdapNetTrailSpec_DTO_OUT[mapasCache
					.getMapaAdapNetTrailSp().get(rcsopAdapTrailSp.getAtrrIdAdapCode()).size()];
			int contArrayAdapNetTrailSpecs = 0;
			for (RcsopAdapNetTrailSp rcsopAdapNetTrailSp : mapasCache.getMapaAdapNetTrailSp()
					.get(rcsopAdapTrailSp.getAtrrIdAdapCode())) {
				AdapNetTrailSpec_DTO_OUT adapNetTrailSpec_DTO_OUT = new AdapNetTrailSpec_DTO_OUT();
				adapNetTrailSpec_DTO_OUT.setAdapCode(rcsopAdapNetTrailSp.getAntsIdAdapCode());
				adapNetTrailSpec_DTO_OUT.setMnemonic(rcsopAdapNetTrailSp.getAntsNaMnemonic());
				AdapAccessSpec_DTO_OUT adapAccessSpec_DTO_OUT = null;
				if (mapasCache.getMapaAdapAccessSpec().containsKey(rcsopAdapNetTrailSp.getRcsopAdapAccSpec().getAacsIdAdapCode())) {
					adapAccessSpec_DTO_OUT = mapasCache.getMapaAdapAccessSpec().get(rcsopAdapNetTrailSp.getRcsopAdapAccSpec().getAacsIdAdapCode());
				} else {
					adapAccessSpec_DTO_OUT = 
							creaAdapAccesSpec(mapasCache, rcsopAdapNetTrailSp.getRcsopAdapAccSpec().getAacsIdAdapCode(), rcsopAdapNetTrailSp.getRcsopAdapAccSpec().getAacsNaMnemonic());
				}
				adapNetTrailSpec_DTO_OUT.setAdapAccessSpec(adapAccessSpec_DTO_OUT);

				LOGGER.info("INI - listaRcsorAdNetTrUseEqRo ");
				if (mapasCache.getMapaAdNetTrUseEqRo().get(rcsopAdapNetTrailSp.getAntsIdAdapCode()) != null) {
					AdapNetTrailUseEquipRole_DTO_OUT[] arrayAdapNetTrailUseEquipRole = new AdapNetTrailUseEquipRole_DTO_OUT[mapasCache
							.getMapaAdNetTrUseEqRo().get(rcsopAdapNetTrailSp.getAntsIdAdapCode()).size()];
					int contArrayAdapNetTrailUseEquipRole = 0;
					for (RcsorAdNetTrUseEqRo rcsorAdNetTrUseEqRo : mapasCache.getMapaAdNetTrUseEqRo()
							.get(rcsopAdapNetTrailSp.getAntsIdAdapCode())) {
						AdapNetTrailUseEquipRole_DTO_OUT adapNetTrailUseEquipRole_DTO_OUT = new AdapNetTrailUseEquipRole_DTO_OUT();
						adapNetTrailUseEquipRole_DTO_OUT
								.setMinCardinality(rcsorAdNetTrUseEqRo.getNterQuMinCardinality());
						adapNetTrailUseEquipRole_DTO_OUT
								.setMaxCardinality(rcsorAdNetTrUseEqRo.getNterQuMaxCardinality());
						AdapEquipmentRoleSpec_DTO_OUT adapEquipmentRoleSpec = null;
						if (mapasCache.getMapaAdapEquipmentRoleSpecDTO().containsKey(rcsorAdNetTrUseEqRo.getRcsopAdapEquipRoleSp().getAersIdAdapCode())){
							adapEquipmentRoleSpec = mapasCache.getMapaAdapEquipmentRoleSpecDTO().get(rcsorAdNetTrUseEqRo.getRcsopAdapEquipRoleSp().getAersIdAdapCode());
						}else{
							adapEquipmentRoleSpec = 
									creaAdapEquipamnentRoleSpec(mapasCache, rcsorAdNetTrUseEqRo.getRcsopAdapEquipRoleSp().getAersIdAdapCode(), rcsorAdNetTrUseEqRo.getRcsopAdapEquipRoleSp().getAersNaMnemonic());
						}
						adapNetTrailUseEquipRole_DTO_OUT.setAdapEquipmentRoleSpec(adapEquipmentRoleSpec);
						arrayAdapNetTrailUseEquipRole[contArrayAdapNetTrailUseEquipRole] = adapNetTrailUseEquipRole_DTO_OUT;
						contArrayAdapNetTrailUseEquipRole++;
					}
					adapNetTrailSpec_DTO_OUT.setAdapNetTrailUseEquipRoles(arrayAdapNetTrailUseEquipRole);
				}
				LOGGER.info("FIN - listaRcsorAdNetTrUseEqRo ");

				LOGGER.info("INI - listaRcsorAdNetTrUseFaSp ");
				if (mapasCache.getMapaAdNetTrUseFaSp().get(rcsopAdapNetTrailSp.getAntsIdAdapCode()) != null) {
					AdapNetTrailUseFacilitySp_DTO_OUT[] arrayAdapNetTrailUseFacilitySp_DTO_OUT = new AdapNetTrailUseFacilitySp_DTO_OUT[mapasCache
							.getMapaAdNetTrUseFaSp().get(rcsopAdapNetTrailSp.getAntsIdAdapCode()).size()];
					int contArrayAdapNetTrailUseFacilitySp = 0;
					for (RcsorAdNetTrUseFaSp rcsorAdNetTrUseFaSp : mapasCache.getMapaAdNetTrUseFaSp()
							.get(rcsopAdapNetTrailSp.getAntsIdAdapCode())) {
						AdapNetTrailUseFacilitySp_DTO_OUT adapNetTrailUseFacilitySp_DTO_OUT = new AdapNetTrailUseFacilitySp_DTO_OUT();
						adapNetTrailUseFacilitySp_DTO_OUT
								.setMaxCardinality(rcsorAdNetTrUseFaSp.getAnfsQuMaxCardinality());
						adapNetTrailUseFacilitySp_DTO_OUT
								.setMinCardinality(rcsorAdNetTrUseFaSp.getAnfsQuMinCardinality());
						AdapFacilitySpec_DTO_OUT adapFacilitySpec = null;
						if (mapasCache.getMapaAdapFacilitySpecDTOOUT()
								.containsKey(rcsorAdNetTrUseFaSp.getRcsopAdapFacSpec().getAfspIdAdapCode())) {
							adapFacilitySpec = mapasCache.getMapaAdapFacilitySpecDTOOUT()
									.get(rcsorAdNetTrUseFaSp.getRcsopAdapFacSpec().getAfspIdAdapCode());
						} else {
							adapFacilitySpec = creaAdapFacilitySpec(mapasCache,
									rcsorAdNetTrUseFaSp.getRcsopAdapFacSpec().getAfspIdAdapCode());
						}
						adapNetTrailUseFacilitySp_DTO_OUT.setAdapFacilitySpec(adapFacilitySpec);
						arrayAdapNetTrailUseFacilitySp_DTO_OUT[contArrayAdapNetTrailUseFacilitySp] = adapNetTrailUseFacilitySp_DTO_OUT;
						contArrayAdapNetTrailUseFacilitySp++;
					}

					adapNetTrailSpec_DTO_OUT.setAdapNetTrailUseFacilitySps(arrayAdapNetTrailUseFacilitySp_DTO_OUT);
				}
				LOGGER.info("FIN - listaRcsorAdNetTrUseFaSp ");

				if (rcsopAdapNetTrailSp.getRcsopAdapTransportSpec() != null) {
					AdapTransportSpec_DTO_OUT rcsopAdapTransportSpec = new AdapTransportSpec_DTO_OUT();
					rcsopAdapTransportSpec
							.setAdapCode(rcsopAdapNetTrailSp.getRcsopAdapTransportSpec().getAtrsIdAdapCode());
					rcsopAdapTransportSpec.setCompoundResourceSpecCompositeId(
							rcsopAdapNetTrailSp.getRcsopAdapTransportSpec().getRsspIdResourceSpec());
					rcsopAdapTransportSpec
							.setMnemonic(rcsopAdapNetTrailSp.getRcsopAdapTransportSpec().getAtrsNaMnemonic());
					adapNetTrailSpec_DTO_OUT.setAdapTransportSpec(rcsopAdapTransportSpec);
				}

				arrayAdapNetTrailSpecs[contArrayAdapNetTrailSpecs] = adapNetTrailSpec_DTO_OUT;
				contArrayAdapNetTrailSpecs++;
			}
			adapTrailSpec_DTO_OUT.setAdapNetTrailSpecs(arrayAdapNetTrailSpecs);
		}
		LOGGER.info("INI - listaRcsopAdapNetTrailSp ");

		LOGGER.info("INI - listaRcsorAdTrailSpULrSp ");
		if (mapasCache.getMapaAdTrailSpULrSp().get(rcsopAdapTrailSp.getAtrrIdAdapCode()) != null) {
			Long[] arrayLogicalResourceSpecCompositeIds = new Long[mapasCache.getMapaAdTrailSpULrSp()
					.get(rcsopAdapTrailSp.getAtrrIdAdapCode()).size()];
			int contArrayLogicalResourceSpecCompositeIds = 0;
			for (RcsorAdTrailSpULrSp rcsorAdTrailSpULrSp : mapasCache.getMapaAdTrailSpULrSp()
					.get(rcsopAdapTrailSp.getAtrrIdAdapCode())) {
				arrayLogicalResourceSpecCompositeIds[contArrayLogicalResourceSpecCompositeIds] = rcsorAdTrailSpULrSp
						.getId().getRsspIdResourceSpec();
				contArrayLogicalResourceSpecCompositeIds++;
			}
			adapTrailSpec_DTO_OUT.setLogicalResourceSpecCompositeIds(arrayLogicalResourceSpecCompositeIds);
		}

		if (null == mapasCache.getMapaAdapTrailSpecDTO().get(rcsopAdapTrailSp.getAtrrIdAdapCode())) {
			mapasCache.getMapaAdapTrailSpecDTO().put(rcsopAdapTrailSp.getAtrrIdAdapCode(), adapTrailSpec_DTO_OUT);
		}

		LOGGER.info("INI - listaRcsorAdTrailSpULrSp ");
		return adapTrailSpec_DTO_OUT;
	}

	public InsertAdapXML_OUT insertAdapXML(InsertAdapXML_IN insertadapxml_in, TE_Cabecera te_Cabecera,
			TE_Metadatos te_Metadatos) throws TE_Excepcion {

		InsertAdapXML_OUT out = new InsertAdapXML_OUT();

		try {
			String idAdNetLayerSp = insertadapxml_in.getAdapXML().getAdapNetworkLayerSpec().getAdapCode();
			RcsopAdNetLayerSp adNetLayerSp = rcsopAdNetLayerSpRepository.getOne(idAdNetLayerSp);
			Long idAdapXmlType = insertadapxml_in.getAdapXML().getAdapXMLType().getId();
			RcsodAdapXmlType adapXmlType = rcsodAdapXmlTypeRepository.getOne(idAdapXmlType);
			RcsopAdapXml adapXml = new RcsopAdapXml();
			if (null != insertadapxml_in.getAdapXML().getAdapOperation()) {
				Long idAdapOpe = insertadapxml_in.getAdapXML().getAdapOperation().getId();
				if (idAdapOpe != null) {
					RcsopAdapOperation adapOpe = rcsopAdapOperationRepository.findOne(idAdapOpe);
					adapXml.setRcsopAdapOperation(adapOpe);
				}
			}
			BigDecimal idAdapXML = new BigDecimal(insertadapxml_in.getAdapXML().getAdapIdRequest().longValue());
			adapXml.setRcsopAdNetLayerSp(adNetLayerSp);
			adapXml.setRcsodAdapXmlType(adapXmlType);
			adapXml.setAxmlIdAdapIdRequest(idAdapXML);
			String xmlDo = insertadapxml_in.getAdapXML().getXml();
			adapXml.setAxmlDoXml(xmlDo);
			BigDecimal userId = new BigDecimal(te_Cabecera.getIdUsuario());
			adapXml.setUserIdCreatorParty(userId);
			Timestamp fechaActual = new Timestamp(new Date().getTime());
			adapXml.setAxmlTiXmlDate(fechaActual);
			adapXml.setAudiTiCreation(fechaActual);
			if (null != insertadapxml_in.getAdapXML().getOrchestrationPlanTask()) {
				adapXml.setOpelIdOrchPlanElement(insertadapxml_in.getAdapXML().getOrchestrationPlanTask().getId());
			}

			AdapXML_DTO_OUT adapXmlOut = new AdapXML_DTO_OUT();
			if (insertadapxml_in.isGenerateIdMN() != null && insertadapxml_in.isGenerateIdMN()) {
				GetADCOREQMNSEQDaoOut seqValue = rcsopAdapXmlRepository.GetADCOREQMNSEQ(sequence);
				adapXml.setAxmlIdReqMn(seqValue.getCode().longValue());
				adapXmlOut.setAdapIdRequestMN(seqValue.getCode().longValue());
			}
			RcsopAdapXml res = rcsopAdapXmlRepository.save(adapXml);
			adapXmlOut.setId(res.getAxmlIdAdapXml());
			out.setAdapXML(adapXmlOut);

		} catch (PersistenceException | JpaObjectRetrievalFailureException e) {
			throw new TE_Excepcion(ERROR_000112, DESCRIPTION_000112);
		}

		return out;
	}

	/**
	 * Metodo en el que se inserta todo el modelo
	 * 
	 * @param insertwholeadaptationmodel_in
	 * @param te_Cabecera
	 * @param te_Metadatos
	 * @return InsertWholeAdaptationModel_OUT
	 * @throws TE_Excepcion
	 */
	public InsertWholeAdaptationModel_OUT insertWholeAdaptationModel(
			InsertWholeAdaptationModel_IN insertwholeadaptationmodel_in, TE_Cabecera te_Cabecera,
			TE_Metadatos te_Metadatos) throws TE_Excepcion {
		LOGGER.info("INICIO AdapModelServiceImpl.insertWholeAdaptationModel entra al servicio");
		if (insertwholeadaptationmodel_in == null) {
			LOGGER.info("FIN AdapModelServiceImpl.insertWholeAdaptationModel entra al servicio");
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}
		// entrada a 1
		List<RcsopAdNetLayerSp> listaRcsopAdNetLayerSp = new ArrayList<RcsopAdNetLayerSp>();
		// entrada a 2
		List<RcsodAdapOperationType> listaRcsodAdapOperationType = new ArrayList<RcsodAdapOperationType>();
		// entrada a 3
		List<RcsopAdapOperation> listaRcsopAdapOperation = new ArrayList<RcsopAdapOperation>();
		// entrada a 4
		List<RcsoaAdOpTran> listaRcsoaAdOpTran = new ArrayList<RcsoaAdOpTran>();
		// entrada a 5
		List<RcsodLockVoiceType> listaRcsodLockVoiceType = new ArrayList<RcsodLockVoiceType>();
		// entrada a 6
		List<RcsorAdNetLaySpExeOp> listaRcsorAdNetLaySpExeOp = new ArrayList<RcsorAdNetLaySpExeOp>();
		// entrada a 7
		List<RcsopAdapSerLineSp> listaRcsopAdapSerLineSp = new ArrayList<RcsopAdapSerLineSp>();
		// entrada a 8
		List<RcsorAdapSeLiTrRfsSp> listaRcsorAdapSeLiTrRfsSp = new ArrayList<RcsorAdapSeLiTrRfsSp>();
		// entrada a 9
		List<RcsopAdapNetSerSp> listaRcsopAdapNetSerSp = new ArrayList<RcsopAdapNetSerSp>();
		// entrada a 10
		List<RcsodAdNetSeTypeSp> listaRcsodAdNetSeTypeSp = new ArrayList<RcsodAdNetSeTypeSp>();
		// entrada a 11
		List<RcsorAdNetSSpSuby> listaRcsorAdNetSSpSuby = new ArrayList<RcsorAdNetSSpSuby>();
		// entrada a 12
		List<RcsorAdNSerTrRfsSp> listaRcsorAdNSerTrRfsSp = new ArrayList<RcsorAdNSerTrRfsSp>();
		// entrada a 13
		List<RcsorAdNetSSPTby> listaRcsorAdNetSSPTby = new ArrayList<RcsorAdNetSSPTby>();
		// entrada a 14
		List<RcsorAdNetSSpTbyRec> listaRcsorAdNetSSpTbyRec = new ArrayList<RcsorAdNetSSpTbyRec>();
		// entrada a 15
		List<RcsorAdNSspUseFacSp> listaRcsorAdNSspUseFacSp = new ArrayList<RcsorAdNSspUseFacSp>();
		// entrada a 16
		List<RcsorAdSeliSpUseNetse> listaRcsorAdSeliSpUseNetse = new ArrayList<RcsorAdSeliSpUseNetse>();
		// entrada a 17
		List<RcsorAdapNetSeSpRel> listaRcsorAdapNetSeSpRel = new ArrayList<RcsorAdapNetSeSpRel>();
		// entrada a 18
		List<RcsopAdapEquipRoleSp> listaRcsopAdapEquipRoleSp = new ArrayList<RcsopAdapEquipRoleSp>();
		// entrada a 19
		List<RcsorAdEqRoRelIsTriBy> listaRcsorAdEqRoRelIsTriBy = new ArrayList<RcsorAdEqRoRelIsTriBy>();
		// entrada a 20
		List<RcsorANetSeHEquRoSp> listaRcsorANetSeHEquRoSp = new ArrayList<RcsorANetSeHEquRoSp>();
		// entrada a 21
		List<RcsodAdNetSerSpReTy> listaRcsodAdNetSerSpReTy = new ArrayList<RcsodAdNetSerSpReTy>();
		// entrada a 22
		List<RcsoaAdTaskStepSp> listaRcsoaAdTaskStepSp = new ArrayList<RcsoaAdTaskStepSp>();
		// entrada a 23
		List<RcsopAdapAccSpec> listaRcsopAdapAccSpec = new ArrayList<RcsopAdapAccSpec>();
		// entrada a 24
		List<RcsorAdAcSpURSpChVal> listaRcsorAdAcSpURSpChVal = new ArrayList<RcsorAdAcSpURSpChVal>();
		// entrada a 25
		List<RcsorAdAccSpULogRSp> listaRcsorAdAccSpULogRSp = new ArrayList<RcsorAdAccSpULogRSp>();
		// entrada a 26
		List<RcsorAdSerLiUseAcSp> listaRcsorAdSerLiUseAcSp = new ArrayList<RcsorAdSerLiUseAcSp>();
		// entrada al 27
		List<RcsorAdAccUseEqRoSp> listaRcsorAdAccUseEqRoSp = new ArrayList<RcsorAdAccUseEqRoSp>();
		// entrada al 28
		List<RcsodAdapFaSpecType> listaRcsodAdapFaSpecType = new ArrayList<RcsodAdapFaSpecType>();
		// entrada a 29
		List<RcsopAdapFacSpec> listaRcsopAdapFacSpec = new ArrayList<RcsopAdapFacSpec>();
		// entrada a 30
		List<RcsorAdFaSHResS> listaRcsorAdFaSHResS = new ArrayList<RcsorAdFaSHResS>();
		// entrada al 31
		List<RcsorAdFaPsChVa> listaRcsorAdFaPsChVa = new ArrayList<RcsorAdFaPsChVa>();
		// entrada a 32
		List<RcsorAdFacSpTrtoRsc> listaRcsorAdFacSpTrtoRsc = new ArrayList<RcsorAdFacSpTrtoRsc>();
		// entrada a 33
		List<RcsorAdFacSpTrSeSCv> listaRcsorAdFacSpTrSeSCv = new ArrayList<RcsorAdFacSpTrSeSCv>();
		// entrada a 34
		List<RcsorAdFacSpTrtoEi> listaRcsorAdFacSpTrtoEi = new ArrayList<RcsorAdFacSpTrtoEi>();
		// entrada a 35
		List<RcsorAdEqRoUseFacSp> listaRcsorAdEqRoUseFacSp = new ArrayList<RcsorAdEqRoUseFacSp>();
		// entrada a 36
		List<RcsorAdSlSpUseFaSp> listaRcsorAdSlSpUseFaSp = new ArrayList<RcsorAdSlSpUseFaSp>();
		// entrada a 37
		List<RcsorAdapFacSpRel> listaRcsorAdapFacSpRel = new ArrayList<RcsorAdapFacSpRel>();
		// entrada a 38
		List<RcsorAdAccUseFacSpec> listaRcsorAdAccUseFacSpec = new ArrayList<RcsorAdAccUseFacSpec>();
		// entrada a 39
		List<RcsopAdFacValueSpec> listaRcsopAdFacValueSpec = new ArrayList<RcsopAdFacValueSpec>();
		// entrada a 40
		List<RcsorAdFVaSpTrtoGa> listaRcsorAdFVaSpTrtoGa = new ArrayList<RcsorAdFVaSpTrtoGa>();
		// entrada a 41
		List<RcsorAdapFacVaSusby> listaRcsorAdapFacVaSusby = new ArrayList<RcsorAdapFacVaSusby>();
		// entrada a 42
		List<RcsorAdFaVaPsChVa> listaRcsorAdFaVaPsChVa = new ArrayList<RcsorAdFaVaPsChVa>();
		// entrada a 43
		List<RcsorAdFVaSpTrtoSsc> listaRcsorAdFVaSpTrtoSsc = new ArrayList<RcsorAdFVaSpTrtoSsc>();
		// entrada a 44
		List<RcsorAdFVaSpTrtoRsc> listaRcsorAdFVaSpTrtoRsc = new ArrayList<RcsorAdFVaSpTrtoRsc>();
		// entrada a 45
		List<RcsopAdapTransportSpec> listaRcsopAdapTransportSpec = new ArrayList<RcsopAdapTransportSpec>();
		// entrada a 46
		List<RcsopAdapTrailSp> listaRcsopAdapTrailSp = new ArrayList<RcsopAdapTrailSp>();
		// entrada a 48
		List<RcsorAdNSeReqTrailSp> listaRcsorAdNSeReqTrailSp = new ArrayList<RcsorAdNSeReqTrailSp>();
		// entrada a 49
		List<RcsopAdapNetTrailSp> listaRcsopAdapNetTrailSp = new ArrayList<RcsopAdapNetTrailSp>();
		// entrada a 50
		List<RcsorAdNetTrUseFaSp> listaRcsorAdNetTrUseFaSp = new ArrayList<RcsorAdNetTrUseFaSp>();
		// entrada a 51
		List<RcsorAdNetTrUseEqRo> listaRcsorAdNetTrUseEqRo = new ArrayList<RcsorAdNetTrUseEqRo>();
		// entrada a 52
		List<RcsopAdapParameter> listaRcsopAdapParameter = new ArrayList<RcsopAdapParameter>();
		// entrada a 53
		List<RcsorAdTrailSpULrSp> listaRcsorAdTrailSpULrSp = new ArrayList<RcsorAdTrailSpULrSp>();
		// entrada a 54
		List<RcsorNetLayerHTrail> listaRcsorNetLayerHTrail = new ArrayList<RcsorNetLayerHTrail>();
		// entrada a 55
		List<RcsorNetLayHasSeLine> listaRcsorNetLayHasSeLine = new ArrayList<RcsorNetLayHasSeLine>();
		// entrada a 56
		List<RcsorNetLayUseAc> listaRcsorNetLayUseAc = new ArrayList<RcsorNetLayUseAc>();
		// entrada a 57
		List<RcsorAdapNssIsTrigBy> listaRcsorAdapNssIsTrigBy = new ArrayList<RcsorAdapNssIsTrigBy>();
		// entrada a 58
		List<RcsorNetLayerHasFac> listaRcsorNetLayerHasFac = new ArrayList<RcsorNetLayerHasFac>();
		// entrada a 59
		List<RcsorNeLayerHNetServ> listaRcsorNeLayerHNetServ = new ArrayList<RcsorNeLayerHNetServ>();
		// entrada a 60
		List<RcsorNeLayHEqRole> listaRcsorNeLayHEqRole = new ArrayList<RcsorNeLayHEqRole>();
		// entrada a 61
		List<RcsorAdNssHasFacVal> listaRcsorAdNssHasFacVal = new ArrayList<RcsorAdNssHasFacVal>();

		// Fix 38
		List<String> fix38 = new ArrayList<String>();
		// Fix 27
		List<String> fix27 = new ArrayList<String>();
		// Fix 48
		List<String> fix48 = new ArrayList<String>();
		// Fix 50
		List<String> fix50 = new ArrayList<String>();
		// Fix 51
		List<String> fix51 = new ArrayList<String>();
		// Fix 26
		List<String> fix26 = new ArrayList<String>();
		// Fix 16
		List<String> fix16 = new ArrayList<String>();
		// Fix 32
		List<String> fix32 = new ArrayList<String>();
		// Fix 33
		List<String> fix33 = new ArrayList<String>();
		// Fix 35
		List<String> fix35 = new ArrayList<String>();
		// Fix 36
		List<String> fix36 = new ArrayList<String>();
		// Fix 37
		List<String> fix37 = new ArrayList<String>();
		// Fix 28
		List<Long> fix28 = new ArrayList<Long>();
		// Fix 29
		List<String> fix29 = new ArrayList<String>();
		// Fix 15
		List<String> fix15 = new ArrayList<String>();
		// Fix 20
		List<String> fix20 = new ArrayList<String>();
		// Fix 23
		List<String> fix23 = new ArrayList<String>();
		// Fix 10
		List<String> fix10 = new ArrayList<String>();
		// Fix 6
		List<String> fix6 = new ArrayList<String>();
		// Fix 17
		List<String> fix17 = new ArrayList<String>();
		// Fix 19
		List<String> fix19 = new ArrayList<String>();
		// Fix 18
		List<String> fix18 = new ArrayList<String>();
		// Fix 56
		List<String> fix56 = new ArrayList<String>();
		// Fix 39
		List<String> fix39 = new ArrayList<String>();
		// Fix 1
		List<String> fix1 = new ArrayList<String>();
		// Fix 53
		List<String> fix53 = new ArrayList<String>();
		// Fix 34
		List<String> fix34 = new ArrayList<String>();
		// Fix 54
		List<String> fix54 = new ArrayList<String>();

		// fecha actual
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		// usuario obtenido de la cabecera
		BigDecimal usuario = BigDecimal.ZERO;
		if (te_Cabecera != null) {
			usuario = new BigDecimal(te_Cabecera.getIdUsuario());
		}
		// fecha para ti_end_validity
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Date fecha_default = new Date();
		try {
			fecha_default = dateFormat.parse("09/09/9999 23:59:59");
		} catch (ParseException e1) {
			LOGGER.error(e1.getMessage(), e1);
			throw new TE_Excepcion(ERROR_000101, "Error al parsear la fecha de fin de validación");
		}
		Long time = fecha_default.getTime();
		Timestamp fecha_default_end = new Timestamp(time);

		// objeto a devolver
		InsertWholeAdaptationModel_OUT insertWholeAdaptationModel_OUT = new InsertWholeAdaptationModel_OUT();
		LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel comienza ejecución");

		// utilidades empleadas en el desarrollo
		Set<Long> setLockVoiceType = new HashSet<Long>();
		Set<Long> setAdNetSeTypeSp = new HashSet<Long>();
		Set<Long> setAdapFaSpecType = new HashSet<Long>();
		Set<Long> setAdapFaSpec = new HashSet<Long>();
		Set<Long> setAdapOperationType = new HashSet<Long>();
		Set<String> setAdapTransportSpec = new HashSet<String>();
		Set<String> setAdapTrailSp = new HashSet<String>();

		// obtenemos los parametros de entrada y los ponemos en listas para mas
		// FOR DONDE INSERTE LAS TABLAS BASICAS PRIMERO
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec : insertwholeadaptationmodel_in
				.getAdapNetworkLayerSpecs()) {

			// este for para el 1 (por el camino mas corto posible)
			// COMPROBAR SI YA EXISTE, SI EXISTE NO INSERTAMOS NI ACTUALIZAMOS,
			// AUNQUE SEGUIMOS CON LO SIGUIENTE
			RcsopAdNetLayerSp rcsopAdNetLayerSp;
			try {
				LOGGER.info(
						"AdapModelServiceImpl.insertWholeAdaptationModel llamada a rcsopAdNetLayerSpRepository.findOne");
				rcsopAdNetLayerSp = rcsopAdNetLayerSpRepository.findOne(adapNetworkLayerSpec.getAdapCode());
			} catch (PersistenceException e) {
				LOGGER.error(e.getMessage());
				throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
			}

			if (rcsopAdNetLayerSp == null) {
				rcsopAdNetLayerSp = new RcsopAdNetLayerSp();
				rcsopAdNetLayerSp.setAnlsIdAdapCode(adapNetworkLayerSpec.getAdapCode());
				rcsopAdNetLayerSp.setGenaIdApplication(adapNetworkLayerSpec.getApplicationId());
				rcsopAdNetLayerSp.setUserIdCreatorParty(usuario);
				rcsopAdNetLayerSp.setAudiTiCreation(fechaActual);
				listaRcsopAdNetLayerSp.add(rcsopAdNetLayerSp);
			}

			// incluyo todos los adapCode que vengan (tanto si ya existía en
			// base de datos como si no), para poder usarlo en la tabla 56
			fix1.add(adapNetworkLayerSpec.getAdapCode());

			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel obtenAdapNetLaySpExecutesOp");
			obtenAdapNetLaySpExecutesOp(listaRcsodAdapOperationType, listaRcsopAdapOperation, listaRcsoaAdOpTran,
					listaRcsodLockVoiceType, listaRcsorAdNetLaySpExeOp, fechaActual, usuario, fecha_default_end,
					adapNetworkLayerSpec, rcsopAdNetLayerSp, setLockVoiceType, setAdapOperationType, fix6);
			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel obtenAdapServiceLineSpec");
			obtenAdapServiceLineSpec(listaRcsopAdapSerLineSp, listaRcsorAdapSeLiTrRfsSp, listaRcsorAdSeliSpUseNetse,
					listaRcsorAdSerLiUseAcSp, listaRcsorAdSlSpUseFaSp, fechaActual, usuario, adapNetworkLayerSpec,
					fix26, fix16, fix36, listaRcsorNetLayHasSeLine);
			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel obtenAdapNetworkServiceSpec");
			obtenAdapNetworkServiceSpec(listaRcsopAdapNetSerSp, listaRcsodAdNetSeTypeSp, listaRcsorAdNetSSpSuby,
					listaRcsorAdNSerTrRfsSp, listaRcsorAdNetSSPTby, listaRcsorAdNetSSpTbyRec, listaRcsorAdNSspUseFacSp,
					listaRcsorAdapNetSeSpRel, listaRcsorANetSeHEquRoSp, listaRcsodAdNetSerSpReTy,
					listaRcsoaAdTaskStepSp, listaRcsorAdNSeReqTrailSp, listaRcsorAdapNssIsTrigBy, fechaActual, usuario,
					fecha_default_end, adapNetworkLayerSpec, setAdNetSeTypeSp, fix48, fix15, fix20, fix29, fix10,
					fix17);
			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel obtenAdapAccesSpec");
			obtenAdapAccesSpec(listaRcsopAdapAccSpec, listaRcsorAdAcSpURSpChVal, listaRcsorAdAccSpULogRSp,
					listaRcsorAdAccUseEqRoSp, listaRcsorAdAccUseFacSpec, fechaActual, usuario, adapNetworkLayerSpec,
					fix38, fix27, fix23, fix29);
			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel obtenAdapFacilitySpec");
			obtenAdapFacilitySpec(listaRcsodAdapFaSpecType, listaRcsopAdapFacSpec, listaRcsorAdFaSHResS,
					listaRcsorAdFaPsChVa, listaRcsorAdFacSpTrtoRsc, listaRcsorAdFacSpTrSeSCv, listaRcsorAdFacSpTrtoEi,
					listaRcsorAdapFacSpRel, listaRcsopAdFacValueSpec, listaRcsorAdFVaSpTrtoGa, listaRcsorAdapFacVaSusby,
					listaRcsorAdFaVaPsChVa, listaRcsorAdFVaSpTrtoSsc, listaRcsorAdFVaSpTrtoRsc, fechaActual, usuario,
					fecha_default_end, adapNetworkLayerSpec, setAdapFaSpecType, setAdapFaSpec, fix37, fix29, fix28,
					fix33, fix32, fix39, fix34, listaRcsorAdNssHasFacVal);
			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel obtenAdapTrailSpec");
			obtenAdapTrailSpec(listaRcsopAdapTransportSpec, listaRcsopAdapTrailSp, listaRcsopAdapNetTrailSp,
					listaRcsorAdNetTrUseFaSp, listaRcsorAdNetTrUseEqRo, listaRcsorAdTrailSpULrSp, fechaActual, usuario,
					adapNetworkLayerSpec, setAdapTransportSpec, setAdapTrailSp, fix50, fix51, fix53);
			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel obtenAdapEquipmentRoleSpec");
			obtenAdapEquipmentRoleSpec(listaRcsopAdapEquipRoleSp, listaRcsorAdEqRoRelIsTriBy, listaRcsorAdEqRoUseFacSp,
					listaRcsorNeLayHEqRole, fechaActual, usuario, adapNetworkLayerSpec, fix35, fix29, fix19, fix18);

			rellenarListaRcsorNeLayerHNetServ(listaRcsorNeLayerHNetServ, adapNetworkLayerSpec, fechaActual, usuario);

		}

		rellenarListaRcsopAdapParameter(insertwholeadaptationmodel_in, listaRcsopAdapParameter, fechaActual, usuario);
		rellenarListaRcsorNetLayUseAc(insertwholeadaptationmodel_in, listaRcsorNetLayUseAc, fechaActual, usuario, fix23,
				fix56, fix1);
		rellenarListaRcsorNetLayerHasFac(insertwholeadaptationmodel_in, listaRcsorNetLayerHasFac, fechaActual, usuario);
		rellenarListaRcsorNetLayerHTrail(insertwholeadaptationmodel_in, listaRcsorNetLayerHTrail, fechaActual, usuario,
				fix54);

		// una vez incluidos todos los registros en la lista
		// listaRcsorAdapFacSpRel
		List<RcsorAdapFacSpRel> listaFacSpRelNoExisteClave = new ArrayList<RcsorAdapFacSpRel>();
		for (RcsorAdapFacSpRel elto : listaRcsorAdapFacSpRel) {
			if (!fix29.contains(elto.getRcsopAdapFacSpec1().getAfspIdAdapCode())
					|| !fix29.contains(elto.getRcsopAdapFacSpec2().getAfspIdAdapCode())) {
				listaFacSpRelNoExisteClave.add(elto);
			}
		}

		if (!listaFacSpRelNoExisteClave.isEmpty()) {
			listaRcsorAdapFacSpRel.remove(listaFacSpRelNoExisteClave);
		}

		// una vez he incluido todos los registros en la lista
		// listaRcsorAdEqRoRelIsTriBy compruebo que su foreign key existe
		List<RcsorAdEqRoRelIsTriBy> listaClaveExternaNoExiste = new ArrayList<RcsorAdEqRoRelIsTriBy>();
		for (RcsorAdEqRoRelIsTriBy el : listaRcsorAdEqRoRelIsTriBy) {
			if (!fix18.contains(el.getId().getAersIdAdapCode())) {
				listaClaveExternaNoExiste.add(el);
			}
		}

		// ahora elimino aquello cuya clave foranea no existe
		if (!listaClaveExternaNoExiste.isEmpty()) {
			listaRcsorAdEqRoRelIsTriBy.removeAll(listaClaveExternaNoExiste);
		}

		// Valido una vez que Fix 29 esta rellenada, que la FK-AFSP_ID_ADAP_CODE
		// de la listaRcsorAdAccUseFacSpec existe en Fix29
		List<RcsorAdAccUseFacSpec> listAux = new ArrayList<RcsorAdAccUseFacSpec>();
		for (RcsorAdAccUseFacSpec elto : listaRcsorAdAccUseFacSpec) {
			if (!fix29.contains(elto.getRcsopAdapFacSpec().getAfspIdAdapCode())) {
				listAux.add(elto);
			}
		}
		if (!listAux.isEmpty()) {
			listaRcsorAdAccUseFacSpec.removeAll(listAux);
		}

		// Se valida que los datos de listaRcsorAdNSspUseFacSp existen en Fix29
		List<RcsorAdNSspUseFacSp> listaRcsorAdNSspUseFacSpAux = new ArrayList<RcsorAdNSspUseFacSp>();
		for (RcsorAdNSspUseFacSp rcsorAdNSspUseFacSp : listaRcsorAdNSspUseFacSp) {
			if (!fix29.contains(rcsorAdNSspUseFacSp.getRcsopAdapFacSpec().getAfspIdAdapCode())) {
				listaRcsorAdNSspUseFacSpAux.add(rcsorAdNSspUseFacSp);
			}
		}
		if (!listaRcsorAdNSspUseFacSpAux.isEmpty()) {
			listaRcsorAdNSspUseFacSp.removeAll(listaRcsorAdNSspUseFacSpAux);
		}

		// Se valida que los datos de listaRcsorAdEqRoUseFacSp existen en Fix29
		List<RcsorAdEqRoUseFacSp> listaRcsorAdEqRoUseFacSpAux = new ArrayList<RcsorAdEqRoUseFacSp>();
		for (RcsorAdEqRoUseFacSp rcsorAdEqRoUseFacSp : listaRcsorAdEqRoUseFacSp) {
			if (!fix29.contains(rcsorAdEqRoUseFacSp.getRcsopAdapFacSpec().getAfspIdAdapCode())) {
				listaRcsorAdEqRoUseFacSpAux.add(rcsorAdEqRoUseFacSp);
			}
		}
		if (!listaRcsorAdEqRoUseFacSpAux.isEmpty()) {
			listaRcsorAdEqRoUseFacSp.removeAll(listaRcsorAdEqRoUseFacSpAux);
		}

		try {
			LOGGER.info("AdapModelServiceImpl.insertWholeAdaptationModel Procedemos a insertar");
			realizarInsertsAdaptationModel(listaRcsopAdNetLayerSp, listaRcsodAdapOperationType, listaRcsopAdapOperation,
					listaRcsoaAdOpTran, listaRcsodLockVoiceType, listaRcsorAdNetLaySpExeOp, listaRcsopAdapSerLineSp,
					listaRcsorAdapSeLiTrRfsSp, listaRcsopAdapNetSerSp, listaRcsodAdNetSeTypeSp, listaRcsorAdNetSSpSuby,
					listaRcsorAdNSerTrRfsSp, listaRcsorAdNetSSPTby, listaRcsorAdNetSSpTbyRec, listaRcsorAdNSspUseFacSp,
					listaRcsorAdSeliSpUseNetse, listaRcsorAdapNetSeSpRel, listaRcsopAdapEquipRoleSp,
					listaRcsorAdEqRoRelIsTriBy, listaRcsorANetSeHEquRoSp, listaRcsodAdNetSerSpReTy,
					listaRcsoaAdTaskStepSp, listaRcsopAdapAccSpec, listaRcsorAdAcSpURSpChVal, listaRcsorAdAccSpULogRSp,
					listaRcsorAdSerLiUseAcSp, listaRcsorAdAccUseEqRoSp, listaRcsodAdapFaSpecType, listaRcsopAdapFacSpec,
					listaRcsorAdFaSHResS, listaRcsorAdFaPsChVa, listaRcsorAdFacSpTrtoRsc, listaRcsorAdFacSpTrSeSCv,
					listaRcsorAdFacSpTrtoEi, listaRcsorAdEqRoUseFacSp, listaRcsorAdSlSpUseFaSp, listaRcsorAdapFacSpRel,
					listaRcsorAdAccUseFacSpec, listaRcsopAdFacValueSpec, listaRcsorAdFVaSpTrtoGa,
					listaRcsorAdapFacVaSusby, listaRcsorAdFaVaPsChVa, listaRcsorAdFVaSpTrtoSsc,
					listaRcsorAdFVaSpTrtoRsc, listaRcsopAdapTransportSpec, listaRcsopAdapTrailSp,
					listaRcsorAdNSeReqTrailSp, listaRcsopAdapNetTrailSp, listaRcsorAdNetTrUseFaSp,
					listaRcsorAdNetTrUseEqRo, listaRcsopAdapParameter, listaRcsorAdTrailSpULrSp,
					listaRcsorNetLayerHTrail, listaRcsorNetLayHasSeLine, listaRcsorNetLayUseAc,
					listaRcsorAdapNssIsTrigBy, listaRcsorNetLayerHasFac, listaRcsorNeLayerHNetServ,
					listaRcsorNeLayHEqRole, listaRcsorAdNssHasFacVal, fix29);

		} catch (PersistenceException e) {
			LOGGER.error(e.getMessage());
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}
		LOGGER.info("FIN AdapModelServiceImpl.insertWholeAdaptationModel Termina la ejecución");
		return insertWholeAdaptationModel_OUT;
	}

	private void rellenarListaRcsopAdapParameter(InsertWholeAdaptationModel_IN insertwholeadaptationmodel_in,
			List<RcsopAdapParameter> listaRcsopAdapParameter, Timestamp fechaActual, BigDecimal usuario) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapParameters_DTO_IN adapParameter : insertwholeadaptationmodel_in
				.getAdapParameters()) {

			RcsopAdapParameter rcsopAdapParameter = new RcsopAdapParameter();

			if (adapParameter.getAdapCode().equals("VERSION_CARGA")) {
				rcsopAdapParameter = rcsopAdapParameterRepository.findOne(adapParameter.getAdapCode());
				if (rcsopAdapParameter == null) {
					rcsopAdapParameter = new RcsopAdapParameter();
				}
			}

			rcsopAdapParameter.setAdpaDsAdapParameters(adapParameter.getDescription());
			rcsopAdapParameter.setAdpaIdAdapCode(adapParameter.getAdapCode());
			rcsopAdapParameter.setAdpaNaValue(adapParameter.getValue());
			rcsopAdapParameter.setAudiTiCreation(fechaActual);
			rcsopAdapParameter.setUserIdCreatorParty(usuario);
			listaRcsopAdapParameter.add(rcsopAdapParameter);
		}
	}

	private void rellenarListaRcsorNetLayUseAc(InsertWholeAdaptationModel_IN insertwholeadaptationmodel_in,
			List<RcsorNetLayUseAc> listaRcsorNetLayUseAc, Timestamp fechaActual, BigDecimal usuario, List<String> fix23,
			List<String> fix56, List<String> fix1) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN netLay : insertwholeadaptationmodel_in
				.getAdapNetworkLayerSpecs()) {
			String codeNetworkLayerSpec = netLay.getAdapCode();

			if (fix1.contains(codeNetworkLayerSpec)) {
				for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapAccessSpec_DTO_IN accessSpec : netLay
						.getAdapAccessSpecs()) {
					// este para el 56
					String codeAccessSpec = accessSpec.getAdapCode();
					String key = codeNetworkLayerSpec + codeAccessSpec;
					if (!fix56.contains(key)) {
						if (fix23.contains(accessSpec.getAdapCode())) {
							fix56.add(key);
							RcsorNetLayUseAc aux = new RcsorNetLayUseAc();
							// ponemos la primary key
							RcsorNetLayUseAcPK id = new RcsorNetLayUseAcPK();
							id.setAnlsIdAdapCode(codeNetworkLayerSpec);
							id.setAacsIdAdapCode(codeAccessSpec);
							aux.setId(id);
							aux.setUserIdCreatorParty(usuario);
							aux.setAudiTiCreation(fechaActual);

							listaRcsorNetLayUseAc.add(aux);
						}
					}
				}
			}
		}
	}

	private void rellenarListaRcsorNetLayerHasFac(InsertWholeAdaptationModel_IN insertwholeadaptationmodel_in,
			List<RcsorNetLayerHasFac> listaRcsorNetLayerHasFac, Timestamp fechaActual, BigDecimal usuario) {
		for (AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec : insertwholeadaptationmodel_in
				.getAdapNetworkLayerSpecs()) {
			for (AdapFacilitySpec_DTO_IN adapFacilitySpecs : adapNetworkLayerSpec.getAdapFacilitySpecs()) {
				if (!adapNetworkLayerSpec.getAdapCode().equals(vacio)) {
					RcsorNetLayerHasFac adapNetworkLayerSpec_1 = new RcsorNetLayerHasFac();
					adapNetworkLayerSpec_1.setAudiTiCreation(fechaActual);
					RcsorNetLayerHasFacPK id = new RcsorNetLayerHasFacPK();
					id.setAfspIdAdapCode(adapFacilitySpecs.getAdapCode());
					id.setAnlsIdAdapCode(adapNetworkLayerSpec.getAdapCode());
					adapNetworkLayerSpec_1.setId(id);
					RcsopAdapFacSpec adapFacSpec = new RcsopAdapFacSpec();
					adapFacSpec.setAfspIdAdapCode(adapFacilitySpecs.getAdapCode());
					adapNetworkLayerSpec_1.setRcsopAdapFacSpec(adapFacSpec);
					RcsopAdNetLayerSp adNetLayerSp = new RcsopAdNetLayerSp();
					adNetLayerSp.setAnlsIdAdapCode(adapNetworkLayerSpec.getAdapCode());
					adapNetworkLayerSpec_1.setRcsopAdNetLayerSp(adNetLayerSp);
					adapNetworkLayerSpec_1.setUserIdCreatorParty(usuario);
					listaRcsorNetLayerHasFac.add(adapNetworkLayerSpec_1);
				}
			}
		}
	}

	private void rellenarListaRcsorNetLayerHTrail(InsertWholeAdaptationModel_IN insertwholeadaptationmodel_in,
			List<RcsorNetLayerHTrail> listaRcsorNetLayerHTrail, Timestamp fechaActual, BigDecimal usuario,
			List<String> fix54) {
		for (AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec : insertwholeadaptationmodel_in
				.getAdapNetworkLayerSpecs()) {
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapTrailSpec_DTO_IN adapTrailSpec : adapNetworkLayerSpec
					.getAdapTrailSpecs()) {

				String anls = adapNetworkLayerSpec.getAdapCode();
				String atrr = adapTrailSpec.getAdapCode();
				String key = anls + atrr;

				if (!fix54.contains(key)) {
					if (adapNetworkLayerSpec.getAdapCode() != null && adapTrailSpec.getAdapCode() != null) {
						fix54.add(key);
						RcsorNetLayerHTrail rcsorNetLayerHTrail = new RcsorNetLayerHTrail();

						rcsorNetLayerHTrail.setAudiTiCreation(fechaActual);
						RcsopAdapTrailSp rcsopAdapTrailSp = new RcsopAdapTrailSp();
						rcsopAdapTrailSp.setAtrrIdAdapCode(adapTrailSpec.getAdapCode());
						rcsorNetLayerHTrail.setRcsopAdapTrailSp(rcsopAdapTrailSp);

						RcsopAdNetLayerSp rcsopAdNetLayerSp = new RcsopAdNetLayerSp();
						rcsopAdNetLayerSp.setAnlsIdAdapCode(adapNetworkLayerSpec.getAdapCode());
						rcsorNetLayerHTrail.setRcsopAdNetLayerSp(rcsopAdNetLayerSp);

						rcsorNetLayerHTrail.setUserIdCreatorParty(usuario);

						RcsorNetLayerHTrailPK id = new RcsorNetLayerHTrailPK();
						id.setAnlsIdAdapCode(adapNetworkLayerSpec.getAdapCode());
						id.setAtrrIdAdapCode(adapTrailSpec.getAdapCode());

						rcsorNetLayerHTrail.setId(id);

						listaRcsorNetLayerHTrail.add(rcsorNetLayerHTrail);
					}
				}
			}
		}
	}

	private List<RcsorNeLayerHNetServ> rellenarListaRcsorNeLayerHNetServ(
			List<RcsorNeLayerHNetServ> listaRcsorNeLayerHNetServ,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			Timestamp fechaActual, BigDecimal usuario) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkServiceSpec_DTO_IN adapNetworkServiceSpec : adapNetworkLayerSpec
				.getAdapNetworkServiceSpecs()) {
			if (adapNetworkLayerSpec.getAdapCode() != null && adapNetworkServiceSpec.getAdapCode() != null) {
				RcsorNeLayerHNetServ rcsorNeLayerHNetServ = new RcsorNeLayerHNetServ();
				RcsorNeLayerHNetServPK id = new RcsorNeLayerHNetServPK();
				id.setAnlsIdAdapCode(adapNetworkLayerSpec.getAdapCode());
				id.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				rcsorNeLayerHNetServ.setId(id);
				RcsopAdapNetSerSp rcsopAdapNetSerSpAux = new RcsopAdapNetSerSp();
				rcsopAdapNetSerSpAux.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				rcsorNeLayerHNetServ.setRcsopAdapNetSerSp(rcsopAdapNetSerSpAux);
				rcsorNeLayerHNetServ.setUserIdCreatorParty(usuario);
				rcsorNeLayerHNetServ.setAudiTiCreation(fechaActual);
				listaRcsorNeLayerHNetServ.add(rcsorNeLayerHNetServ);
			}
		}
		return listaRcsorNeLayerHNetServ;
	}

	private void obtenAdapEquipmentRoleSpec(List<RcsopAdapEquipRoleSp> listaRcsopAdapEquipRoleSp,
			List<RcsorAdEqRoRelIsTriBy> listaRcsorAdEqRoRelIsTriBy, List<RcsorAdEqRoUseFacSp> listaRcsorAdEqRoUseFacSp,
			List<RcsorNeLayHEqRole> listaRcsorNeLayHEqRole, Timestamp fechaActual, BigDecimal usuario,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			List<String> fix35, List<String> fix29, List<String> fix19, List<String> fix18) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapEquipmentRoleSpec_DTO_IN adapEquipmentRoleSpec : adapNetworkLayerSpec
				.getAdapEquipmentRoleSpecs()) {
			// este para el 18
			RcsopAdapEquipRoleSp rcsopAdapEquipRoleSp = new RcsopAdapEquipRoleSp();
			rcsopAdapEquipRoleSp.setAersIdAdapCode(adapEquipmentRoleSpec.getAdapCode());
			rcsopAdapEquipRoleSp.setAersNaMnemonic(adapEquipmentRoleSpec.getMnemonic());
			rcsopAdapEquipRoleSp.setUserIdCreatorParty(usuario);
			rcsopAdapEquipRoleSp.setAudiTiCreation(fechaActual);
			listaRcsopAdapEquipRoleSp.add(rcsopAdapEquipRoleSp);
			fix18.add(adapEquipmentRoleSpec.getAdapCode());

			// este para el 19
			for (int i = 0; i < adapEquipmentRoleSpec.getResourceRoleSpecificationIdsLength(); i++) {
				String key = adapEquipmentRoleSpec.getAdapCode()
						+ adapEquipmentRoleSpec.getResourceRoleSpecificationIds(i).toString();
				if (!fix19.contains(key)) { // para no incluir repetidos
					fix19.add(key);
					RcsorAdEqRoRelIsTriBy rcsorAdEqRoRelIsTriBy = new RcsorAdEqRoRelIsTriBy();
					RcsorAdEqRoRelIsTriByPK rcsorAdEqRoRelIsTriByPK = new RcsorAdEqRoRelIsTriByPK();
					rcsorAdEqRoRelIsTriByPK.setAersIdAdapCode(adapEquipmentRoleSpec.getAdapCode());
					rcsorAdEqRoRelIsTriByPK
							.setRrosIdResourceRoleSpec(adapEquipmentRoleSpec.getResourceRoleSpecificationIds(i));
					rcsorAdEqRoRelIsTriBy.setId(rcsorAdEqRoRelIsTriByPK);
					rcsorAdEqRoRelIsTriBy.setUserIdCreatorParty(usuario);
					rcsorAdEqRoRelIsTriBy.setAudiTiCreation(fechaActual);
					listaRcsorAdEqRoRelIsTriBy.add(rcsorAdEqRoRelIsTriBy);
				}
			}

			// este para el 35
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapEquipRoleUseFacilitySp_DTO_IN adapEquipRoleUseFacilitySp : adapEquipmentRoleSpec
					.getAdapEquipRoleUseFacilitySps()) {
				String aers = adapEquipmentRoleSpec.getAdapCode();
				if (adapEquipRoleUseFacilitySp.getAdapFacilitySpec() != null) {
					String afsp = adapEquipRoleUseFacilitySp.getAdapFacilitySpec().getAdapCode();
					String key = aers + afsp;
					if (!fix35.contains(key)) {
						// if (fix29.contains(afsp)) {
						fix35.add(key);
						RcsorAdEqRoUseFacSp rcsorAdEqRoUseFacSp = new RcsorAdEqRoUseFacSp();
						RcsopAdapEquipRoleSp rcsopAdapEquipRoleSpAux = new RcsopAdapEquipRoleSp();
						rcsopAdapEquipRoleSpAux.setAersIdAdapCode(adapEquipmentRoleSpec.getAdapCode());
						rcsorAdEqRoUseFacSp.setRcsopAdapEquipRoleSp(rcsopAdapEquipRoleSpAux);
						RcsopAdapFacSpec rcsopAdapFacSpec = new RcsopAdapFacSpec();
						rcsopAdapFacSpec
								.setAfspIdAdapCode(adapEquipRoleUseFacilitySp.getAdapFacilitySpec().getAdapCode());
						rcsorAdEqRoUseFacSp.setRcsopAdapFacSpec(rcsopAdapFacSpec);
						rcsorAdEqRoUseFacSp.setUserIdCreatorParty(usuario);
						rcsorAdEqRoUseFacSp.setAudiTiCreation(fechaActual);
						rcsorAdEqRoUseFacSp.setErufQuMaxCardinality(adapEquipRoleUseFacilitySp.getMaxCardinality());
						rcsorAdEqRoUseFacSp.setErufQuMinCardinality(adapEquipRoleUseFacilitySp.getMinCardinality());
						listaRcsorAdEqRoUseFacSp.add(rcsorAdEqRoUseFacSp);

						// }
					}
				}
			}

			// Esto para la 60
			String anlsIdAdapCode = adapNetworkLayerSpec.getAdapCode();
			RcsopAdNetLayerSp adNetLayerSp = new RcsopAdNetLayerSp();
			adNetLayerSp.setAnlsIdAdapCode(anlsIdAdapCode);
			RcsorNeLayHEqRole aux = new RcsorNeLayHEqRole();
			aux.setRcsopAdNetLayerSp(adNetLayerSp);
			RcsopAdapEquipRoleSp tRcsopAdapEquipRoleSp = new RcsopAdapEquipRoleSp();
			String aersIdAdapCode = adapEquipmentRoleSpec.getAdapCode();
			tRcsopAdapEquipRoleSp.setAersIdAdapCode(aersIdAdapCode);
			aux.setRcsopAdapEquipRoleSp(tRcsopAdapEquipRoleSp);
			aux.setUserIdCreatorParty(usuario);
			aux.setAudiTiCreation(fechaActual);
			RcsorNeLayHEqRolePK id = new RcsorNeLayHEqRolePK();
			id.setAersIdAdapCode(aersIdAdapCode);
			id.setAnlsIdAdapCode(anlsIdAdapCode);
			aux.setId(id);
			listaRcsorNeLayHEqRole.add(aux);
		}
	}

	private void obtenAdapTrailSpec(List<RcsopAdapTransportSpec> listaRcsopAdapTransportSpec,
			List<RcsopAdapTrailSp> listaRcsopAdapTrailSp, List<RcsopAdapNetTrailSp> listaRcsopAdapNetTrailSp,
			List<RcsorAdNetTrUseFaSp> listaRcsorAdNetTrUseFaSp, List<RcsorAdNetTrUseEqRo> listaRcsorAdNetTrUseEqRo,
			List<RcsorAdTrailSpULrSp> listaRcsorAdTrailSpULrSp, Timestamp fechaActual, BigDecimal usuario,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			Set<String> setAdapTransportSpec, Set<String> setAdapTrailSp, List<String> fix50, List<String> fix51,
			List<String> fix53) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapTrailSpec_DTO_IN adapTrailSpec : adapNetworkLayerSpec
				.getAdapTrailSpecs()) {

			// este para el 46 (por el camino mas corto posible) TABLA
			// BASICA
			if (!setAdapTrailSp.contains(adapTrailSpec.getAdapCode())) {
				RcsopAdapTrailSp rcsopAdapTrailSp = new RcsopAdapTrailSp();
				rcsopAdapTrailSp.setAtrrIdAdapCode(adapTrailSpec.getAdapCode());
				rcsopAdapTrailSp.setAtrrNaMnemonic(adapTrailSpec.getMnemonic());
				rcsopAdapTrailSp.setUserIdCreatorParty(usuario);
				rcsopAdapTrailSp.setAudiTiCreation(fechaActual);
				listaRcsopAdapTrailSp.add(rcsopAdapTrailSp);
				setAdapTrailSp.add(adapTrailSpec.getAdapCode());
			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetTrailSpec_DTO_IN adapNetTrailSpec : adapTrailSpec
					.getAdapNetTrailSpecs()) {
				for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapTransportSpec_DTO_IN adapTransportSpec : adapNetTrailSpec
						.getAdapTransportSpecs()) {
					// este para el 45 (por el camino mas corto posible)
					// TABLA BASICA
					if (!setAdapTransportSpec.contains(adapTransportSpec.getAdapCode())) {
						RcsopAdapTransportSpec rcsopAdapTransportSpec = new RcsopAdapTransportSpec();
						rcsopAdapTransportSpec.setAtrsIdAdapCode(adapTransportSpec.getAdapCode());
						rcsopAdapTransportSpec.setAtrsNaMnemonic(adapTransportSpec.getMnemonic());
						rcsopAdapTransportSpec
								.setRsspIdResourceSpec(adapTransportSpec.getCompoundResourceSpecCompositeId());
						rcsopAdapTransportSpec.setUserIdCreatorParty(usuario);
						rcsopAdapTransportSpec.setAudiTiCreation(fechaActual);
						listaRcsopAdapTransportSpec.add(rcsopAdapTransportSpec);
						setAdapTransportSpec.add(adapTransportSpec.getAdapCode());
					}
				}

				// este para el 49
				RcsopAdapNetTrailSp RcsopAdapNetTrailSp = new RcsopAdapNetTrailSp();
				RcsopAdapNetTrailSp.setAntsIdAdapCode(adapNetTrailSpec.getAdapCode());
				// relacion con el AdapTrailSpec.adapCode
				RcsopAdapTrailSp rcsopAdapTrailSpAux = new RcsopAdapTrailSp();
				rcsopAdapTrailSpAux.setAtrrIdAdapCode(adapTrailSpec.getAdapCode());
				RcsopAdapNetTrailSp.setRcsopAdapTrailSp(rcsopAdapTrailSpAux);
				// relacion con el adaptransportSpec
				RcsopAdapTransportSpec rcsopAdapTransportSpec = new RcsopAdapTransportSpec();
				if (adapNetTrailSpec.getAdapTransportSpecs().length > 0) {
					rcsopAdapTransportSpec.setAtrsIdAdapCode(adapNetTrailSpec.getAdapTransportSpecs()[0].getAdapCode());
				}
				RcsopAdapNetTrailSp.setRcsopAdapTransportSpec(rcsopAdapTransportSpec);
				// relacion con el adapAccessSpec.adapCode
				RcsopAdapAccSpec rcsopAdapAccSpec = new RcsopAdapAccSpec();
				if (adapNetTrailSpec.getAdapAccessSpecs().length > 0) {
					rcsopAdapAccSpec.setAacsIdAdapCode(adapNetTrailSpec.getAdapAccessSpecs()[0].getAdapCode());
				}
				RcsopAdapNetTrailSp.setRcsopAdapAccSpec(rcsopAdapAccSpec);
				RcsopAdapNetTrailSp.setUserIdCreatorParty(usuario);
				RcsopAdapNetTrailSp.setAudiTiCreation(fechaActual);
				RcsopAdapNetTrailSp.setAntsNaMnemonic(adapNetTrailSpec.getMnemonic());
				listaRcsopAdapNetTrailSp.add(RcsopAdapNetTrailSp);

				// este para el 50
				for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetTrailUseFacilitySp_DTO_IN adapNetTrailUseFacilitySp : adapNetTrailSpec
						.getAdapNetTrailUseFacilitySps()) {
					if (adapNetTrailUseFacilitySp != null && adapNetTrailUseFacilitySp.getAdapFacilitySpec() != null) {
						String afsp = adapNetTrailUseFacilitySp.getAdapFacilitySpec().getAdapCode();
						String ants = adapNetTrailSpec.getAdapCode();
						String key = afsp + ants;
						if (!fix50.contains(key)) {
							fix50.add(key);
							RcsorAdNetTrUseFaSp rcsorAdNetTrUseFaSp = new RcsorAdNetTrUseFaSp();
							RcsopAdapFacSpec rcsopAdapFacSpec = new RcsopAdapFacSpec();
							rcsopAdapFacSpec
									.setAfspIdAdapCode(adapNetTrailUseFacilitySp.getAdapFacilitySpec().getAdapCode());
							rcsorAdNetTrUseFaSp.setRcsopAdapFacSpec(rcsopAdapFacSpec);
							com.telefonica.gdre.model.RcsopAdapNetTrailSp rcsopAdapNetTrailSpAux = new com.telefonica.gdre.model.RcsopAdapNetTrailSp();
							rcsopAdapNetTrailSpAux.setAntsIdAdapCode(adapNetTrailSpec.getAdapCode());
							rcsorAdNetTrUseFaSp.setRcsopAdapNetTrailSp(rcsopAdapNetTrailSpAux);
							rcsorAdNetTrUseFaSp.setUserIdCreatorParty(usuario);
							rcsorAdNetTrUseFaSp.setAudiTiCreation(fechaActual);
							rcsorAdNetTrUseFaSp.setAnfsQuMaxCardinality(adapNetTrailUseFacilitySp.getMaxCardinality());
							rcsorAdNetTrUseFaSp.setAnfsQuMinCardinality(adapNetTrailUseFacilitySp.getMinCardinality());
							listaRcsorAdNetTrUseFaSp.add(rcsorAdNetTrUseFaSp);
						}
					}
				}

				// este para el 51
				for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetTrailUseEquipRole_DTO_IN adapNetTrailUseEquipRole : adapNetTrailSpec
						.getAdapNetTrailUseEquipRoles()) {
					if (adapNetTrailUseEquipRole != null
							&& adapNetTrailUseEquipRole.getAdapEquipmentRoleSpec() != null) {
						String aers = adapNetTrailUseEquipRole.getAdapEquipmentRoleSpec().getAdapCode();
						String ants = adapNetTrailSpec.getAdapCode();
						String key = aers + ants;
						if (!fix51.contains(key)) {
							fix51.add(key);
							RcsorAdNetTrUseEqRo rcsorAdNetTrUseEqRo = new RcsorAdNetTrUseEqRo();
							com.telefonica.gdre.model.RcsopAdapNetTrailSp rcsopAdapNetTrailSpAux = new com.telefonica.gdre.model.RcsopAdapNetTrailSp();
							rcsopAdapNetTrailSpAux.setAntsIdAdapCode(adapNetTrailSpec.getAdapCode());
							rcsorAdNetTrUseEqRo.setRcsopAdapNetTrailSp(rcsopAdapNetTrailSpAux);
							RcsopAdapEquipRoleSp rcsopAdapEquipRoleSp = new RcsopAdapEquipRoleSp();
							rcsopAdapEquipRoleSp.setAersIdAdapCode(
									adapNetTrailUseEquipRole.getAdapEquipmentRoleSpec().getAdapCode());
							rcsorAdNetTrUseEqRo.setRcsopAdapEquipRoleSp(rcsopAdapEquipRoleSp);
							rcsorAdNetTrUseEqRo.setUserIdCreatorParty(usuario);
							rcsorAdNetTrUseEqRo.setAudiTiCreation(fechaActual);

							// if
							// (adapNetTrailUseEquipRole.getAdapEquipmentRoleSpec().getAdapEquipmentRoleSpec()!=
							// null &&
							// adapNetTrailUseEquipRole.getAdapEquipmentRoleSpec().getAdapEquipmentRoleSpec().getAdapEquipRoleUseFacilitySps(0)!=null){
							// rcsorAdNetTrUseEqRo.setNterQuMaxCardinality(adapNetTrailUseEquipRole.getAdapEquipmentRoleSpec().getAdapEquipmentRoleSpec().getAdapEquipRoleUseFacilitySps(0).getMaxCardinality());
							// rcsorAdNetTrUseEqRo.setNterQuMinCardinality(adapNetTrailUseEquipRole.getAdapEquipmentRoleSpec().getAdapEquipmentRoleSpec().getAdapEquipRoleUseFacilitySps(0).getMinCardinality());
							// }

							rcsorAdNetTrUseEqRo.setNterQuMaxCardinality(adapNetTrailUseEquipRole.getMaxCardinality());
							rcsorAdNetTrUseEqRo.setNterQuMinCardinality(adapNetTrailUseEquipRole.getMinCardinality());
							listaRcsorAdNetTrUseEqRo.add(rcsorAdNetTrUseEqRo);
						}
					}
				}

				// Esto es para la 53
				for (Long id : adapTrailSpec.getLogicalResourceSpecCompositeIds()) {
					Long rsspId = id;
					String atrrId = adapTrailSpec.getAdapCode();
					String key = atrrId + rsspId.toString();
					if (!fix53.contains(key)) {
						fix53.add(key);
						RcsorAdTrailSpULrSp rcsorAdTrailSpULrSp = new RcsorAdTrailSpULrSp();
						RcsorAdTrailSpULrSpPK idRcsorAdTrailSpULrSp = new RcsorAdTrailSpULrSpPK();
						idRcsorAdTrailSpULrSp.setRsspIdResourceSpec(rsspId);
						idRcsorAdTrailSpULrSp.setAtrrIdAdapCode(atrrId);
						rcsorAdTrailSpULrSp.setId(idRcsorAdTrailSpULrSp);

						RcsopAdapTrailSp rcsopAdapTrailSp = new RcsopAdapTrailSp();
						rcsopAdapTrailSp.setAtrrIdAdapCode(atrrId);
						rcsorAdTrailSpULrSp.setRcsopAdapTrailSp(rcsopAdapTrailSp);

						rcsorAdTrailSpULrSp.setAudiTiCreation(fechaActual);
						rcsorAdTrailSpULrSp.setUserIdCreatorParty(usuario);

						listaRcsorAdTrailSpULrSp.add(rcsorAdTrailSpULrSp);
					}
				}

			}
		}
	}

	private void obtenAdapFacilitySpec(List<RcsodAdapFaSpecType> listaRcsodAdapFaSpecType,
			List<RcsopAdapFacSpec> listaRcsopAdapFacSpec, List<RcsorAdFaSHResS> listaRcsorAdFaSHResS,
			List<RcsorAdFaPsChVa> listaRcsorAdFaPsChVa, List<RcsorAdFacSpTrtoRsc> listaRcsorAdFacSpTrtoRsc,
			List<RcsorAdFacSpTrSeSCv> listaRcsorAdFacSpTrSeSCv, List<RcsorAdFacSpTrtoEi> listaRcsorAdFacSpTrtoEi,
			List<RcsorAdapFacSpRel> listaRcsorAdapFacSpRel, List<RcsopAdFacValueSpec> listaRcsopAdFacValueSpec,
			List<RcsorAdFVaSpTrtoGa> listaRcsorAdFVaSpTrtoGa, List<RcsorAdapFacVaSusby> listaRcsorAdapFacVaSusby,
			List<RcsorAdFaVaPsChVa> listaRcsorAdFaVaPsChVa, List<RcsorAdFVaSpTrtoSsc> listaRcsorAdFVaSpTrtoSsc,
			List<RcsorAdFVaSpTrtoRsc> listaRcsorAdFVaSpTrtoRsc, Timestamp fechaActual, BigDecimal usuario,
			Timestamp fecha_default_end,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			Set<Long> setAdapFaSpecType, Set<Long> setAdapFaSpec, List<String> fix37, List<String> fix29,
			List<Long> fix28, List<String> fix33, List<String> fix32, List<String> fix39, List<String> fix34,
			List<RcsorAdNssHasFacVal> listaRcsorAdNssHasFacVal) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapFacilitySpec_DTO_IN adapFacilitySpec : adapNetworkLayerSpec
				.getAdapFacilitySpecs()) {
			// este para el 28 (por el camino mas corto posible) TABLA
			// BASICA
			if (adapFacilitySpec.getAdapFacilitySpecTypeId() != null
					&& !setAdapFaSpecType.contains(adapFacilitySpec.getAdapFacilitySpecTypeId())
					&& !fix28.contains(adapFacilitySpec.getAdapFacilitySpecTypeId())) {
				fix28.add(adapFacilitySpec.getAdapFacilitySpecTypeId());
				RcsodAdapFaSpecType rcsodAdapFaSpecType = new RcsodAdapFaSpecType();
				rcsodAdapFaSpecType.setAfstIdAdapFaSpecType(adapFacilitySpec.getAdapFacilitySpecTypeId());
				rcsodAdapFaSpecType.setAfstTiStartValidity(fechaActual);
				rcsodAdapFaSpecType.setAfstTiEndValidity(fecha_default_end);
				rcsodAdapFaSpecType.setUserIdCreatorParty(usuario);
				rcsodAdapFaSpecType.setAudiTiCreation(fechaActual);
				rcsodAdapFaSpecType.setAfstNaAdapFaSpecType(adapFacilitySpec.getAdapFacilitySpecTypeName());
				listaRcsodAdapFaSpecType.add(rcsodAdapFaSpecType);
				setAdapFaSpecType.add(adapFacilitySpec.getAdapFacilitySpecTypeId());
			}

			// este para el 29

			RcsopAdapFacSpec rcsopAdapFacSpec = new RcsopAdapFacSpec();
			rcsopAdapFacSpec.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
			RcsodAdapFaSpecType rcsodAdapFaSpecTypeAux = new RcsodAdapFaSpecType();
			rcsodAdapFaSpecTypeAux.setAfstIdAdapFaSpecType(adapFacilitySpec.getAdapFacilitySpecTypeId());
			rcsopAdapFacSpec.setRcsodAdapFaSpecType(rcsodAdapFaSpecTypeAux);
			rcsopAdapFacSpec.setUserIdCreatorParty(usuario);
			rcsopAdapFacSpec.setAudiTiCreation(fechaActual);
			rcsopAdapFacSpec.setAfspNaMnemonic(adapFacilitySpec.getMnemonic());
			if (adapFacilitySpec.getAdapCode() != null) {
				if (!fix29.contains(adapFacilitySpec.getAdapCode())) {
					fix29.add(adapFacilitySpec.getAdapCode());
					listaRcsopAdapFacSpec.add(rcsopAdapFacSpec);
				}
			}

			// este para el 30
			for (int i = 0; i < adapFacilitySpec.getRestrictionSpecIdsLength(); i++) {
				RcsorAdFaSHResS rcsorAdFaSHResS = new RcsorAdFaSHResS();
				RcsorAdFaSHResSPK rcsorAdFaSHResSPK = new RcsorAdFaSHResSPK();
				rcsorAdFaSHResSPK.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
				rcsorAdFaSHResSPK.setBrspIdRestrictionSpec(adapFacilitySpec.getRestrictionSpecIds(i));
				rcsorAdFaSHResS.setId(rcsorAdFaSHResSPK);
				rcsorAdFaSHResS.setUserIdCreatorParty(usuario);
				rcsorAdFaSHResS.setAudiTiCreation(fechaActual);
				listaRcsorAdFaSHResS.add(rcsorAdFaSHResS);
			}

			// este para el 31
			for (int i = 0; i < adapFacilitySpec.getProductSpecCharacteristicValueIdsLength(); i++) {
				RcsorAdFaPsChVa rcsorAdFaPsChVa = new RcsorAdFaPsChVa();
				RcsorAdFaPsChVaPK rcsorAdFaPsChVaPK = new RcsorAdFaPsChVaPK();
				rcsorAdFaPsChVaPK.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
				rcsorAdFaPsChVaPK.setPsvaIdProdSpecCharVal(adapFacilitySpec.getProductSpecCharacteristicValueIds(i));
				rcsorAdFaPsChVa.setId(rcsorAdFaPsChVaPK);
				rcsorAdFaPsChVa.setUserIdCreatorParty(usuario);
				rcsorAdFaPsChVa.setAudiTiCreation(fechaActual);
				listaRcsorAdFaPsChVa.add(rcsorAdFaPsChVa);
			}

			// este para el 32
			for (int i = 0; i < adapFacilitySpec.getResourceSpecCharacteristicIdsLength(); i++) {
				String key = adapFacilitySpec.getAdapCode();
				if (!fix32.contains(key)) {
					fix32.add(key);
					RcsorAdFacSpTrtoRsc rcsorAdFacSpTrtoRsc = new RcsorAdFacSpTrtoRsc();
					RcsorAdFacSpTrtoRscPK rcsorAdFacSpTrtoRscPK = new RcsorAdFacSpTrtoRscPK();
					rcsorAdFacSpTrtoRscPK.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
					rcsorAdFacSpTrtoRscPK.setRschIdResourceSpChar(adapFacilitySpec.getResourceSpecCharacteristicIds(0));
					rcsorAdFacSpTrtoRsc.setId(rcsorAdFacSpTrtoRscPK);
					rcsorAdFacSpTrtoRsc.setUserIdCreatorParty(usuario);
					rcsorAdFacSpTrtoRsc.setAudiTiCreation(fechaActual);
					listaRcsorAdFacSpTrtoRsc.add(rcsorAdFacSpTrtoRsc);
				}
			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapFacValueSpec_DTO_IN adapFacValueSpec : adapFacilitySpec
					.getAdapFacValueSpecs()) {
				String comprobacion = adapFacilitySpec.getAdapCode() + adapFacValueSpec.getAdapCode();
				if (!fix39.contains(comprobacion)) {
					// tabla 39
					fix39.add(comprobacion);
					RcsopAdFacValueSpec RcsopAdFacValueSpec = new RcsopAdFacValueSpec();
					// falta el id para insertar en esta tabla:
					// AFVS_ID_AD_FAC_VALUE_SPEC
					RcsopAdFacValueSpec.setAfvsIdAdapCode(adapFacValueSpec.getAdapCode());
					RcsopAdapFacSpec rcsopAdapFacSpecAux = new RcsopAdapFacSpec();
					rcsopAdapFacSpecAux.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
					RcsopAdFacValueSpec.setRcsopAdapFacSpec(rcsopAdapFacSpecAux);
					RcsopAdFacValueSpec.setUserIdCreatorParty(usuario);
					RcsopAdFacValueSpec.setAudiTiCreation(fechaActual);
					listaRcsopAdFacValueSpec.add(RcsopAdFacValueSpec);
					GetADCOREQMNSEQDaoOut seqValue = rcsopAdapXmlRepository.GetADCOREQMNSEQ(sequence39);
					BigDecimal idInsert = seqValue.getCode();
					RcsopAdFacValueSpec.setAfvsIdAdFacValueSpec(idInsert.longValue());
					// este para el 40
					for (int i = 0; i < adapFacValueSpec.getGeographicAreaIdsLength(); i++) {
						RcsorAdFVaSpTrtoGa RcsorAdFVaSpTrtoGa = new RcsorAdFVaSpTrtoGa();
						RcsorAdFVaSpTrtoGa.setRcsopAdFacValueSpec(RcsopAdFacValueSpec); // esto
						// para
						// cuando
						// se
						// autogenere
						// el
						// Id,
						// que
						// se
						// guarde
						// aqui,
						// despues
						// lo
						// muevo
						// a
						// su
						// lugar
						RcsorAdFVaSpTrtoGaPK rcsorAdFVaSpTrtoGaPK = new RcsorAdFVaSpTrtoGaPK();
						rcsorAdFVaSpTrtoGaPK.setPlacIdGeographicArea(adapFacValueSpec.getGeographicAreaIds(i));
						rcsorAdFVaSpTrtoGaPK.setAfvsIdAdFacValueSpec(idInsert.longValue());
						RcsorAdFVaSpTrtoGa.setId(rcsorAdFVaSpTrtoGaPK);
						RcsorAdFVaSpTrtoGa.setUserIdCreatorParty(usuario);
						RcsorAdFVaSpTrtoGa.setAudiTiCreation(fechaActual);

						listaRcsorAdFVaSpTrtoGa.add(RcsorAdFVaSpTrtoGa);
					}

					// esto para el 41
					for (int i = 0; i < adapFacValueSpec.getRestrictionSpecIdsLength(); i++) {
						RcsorAdapFacVaSusby RcsorAdapFacVaSusby = new RcsorAdapFacVaSusby();
						RcsorAdapFacVaSusbyPK RcsorAdapFacVaSusbyPK = new RcsorAdapFacVaSusbyPK();
						RcsorAdapFacVaSusby.setRcsopAdFacValueSpec(RcsopAdFacValueSpec);
						RcsorAdapFacVaSusbyPK.setBrspIdRestrictionSpec(adapFacValueSpec.getRestrictionSpecIds(i));
						RcsorAdapFacVaSusbyPK.setAfvsIdAdFacValueSpec(idInsert.longValue());
						RcsorAdapFacVaSusby.setId(RcsorAdapFacVaSusbyPK);
						RcsorAdapFacVaSusby.setUserIdCreatorParty(usuario);
						RcsorAdapFacVaSusby.setAudiTiCreation(fechaActual);
						listaRcsorAdapFacVaSusby.add(RcsorAdapFacVaSusby);
					}

					// esto para el 42
					for (int i = 0; i < adapFacValueSpec.getProductSpecCharacteristicValueIdsLength(); i++) {
						RcsorAdFaVaPsChVa rcsorAdFaVaPsChVa = new RcsorAdFaVaPsChVa();
						RcsorAdFaVaPsChVaPK rcsorAdFaVaPsChVaPK = new RcsorAdFaVaPsChVaPK();
						rcsorAdFaVaPsChVaPK
								.setPsvaIdProdSpecCharVal(adapFacValueSpec.getProductSpecCharacteristicValueIds(i));
						rcsorAdFaVaPsChVaPK.setAfvsIdAdFacValueSpec(idInsert.longValue());
						rcsorAdFaVaPsChVa.setRcsopAdFacValueSpec(RcsopAdFacValueSpec);
						rcsorAdFaVaPsChVa.setId(rcsorAdFaVaPsChVaPK);
						rcsorAdFaVaPsChVa.setUserIdCreatorParty(usuario);
						rcsorAdFaVaPsChVa.setAudiTiCreation(fechaActual);
						listaRcsorAdFaVaPsChVa.add(rcsorAdFaVaPsChVa);
					}

					// esto para el 43
					for (int i = 0; i < adapFacValueSpec.getServiceSpecCharValueUseIdsLength(); i++) {
						RcsorAdFVaSpTrtoSsc rcsorAdFVaSpTrtoSsc = new RcsorAdFVaSpTrtoSsc();
						RcsorAdFVaSpTrtoSscPK rcsorAdFVaSpTrtoSscPK = new RcsorAdFVaSpTrtoSscPK();
						rcsorAdFVaSpTrtoSsc.setRcsopAdFacValueSpec(RcsopAdFacValueSpec);
						rcsorAdFVaSpTrtoSscPK
								.setScvuIdServSpCharValueUse(adapFacValueSpec.getServiceSpecCharValueUseIds(i));
						rcsorAdFVaSpTrtoSscPK.setAfvsIdAdFacValueSpec(idInsert.longValue());
						rcsorAdFVaSpTrtoSsc.setId(rcsorAdFVaSpTrtoSscPK);
						rcsorAdFVaSpTrtoSsc.setUserIdCreatorParty(usuario);
						rcsorAdFVaSpTrtoSsc.setAudiTiCreation(fechaActual);
						listaRcsorAdFVaSpTrtoSsc.add(rcsorAdFVaSpTrtoSsc);
					}

					// esto para el 44
					for (int i = 0; i < adapFacValueSpec.getResourceSpecCharValueUseIdsLength(); i++) {
						RcsorAdFVaSpTrtoRsc RcsorAdFVaSpTrtoRsc = new RcsorAdFVaSpTrtoRsc();
						RcsorAdFVaSpTrtoRscPK rcsorAdFVaSpTrtoRscPK = new RcsorAdFVaSpTrtoRscPK();
						RcsorAdFVaSpTrtoRsc.setRcsopAdFacValueSpec(RcsopAdFacValueSpec);
						rcsorAdFVaSpTrtoRscPK
								.setRsvuIdResSpCharValueUse(adapFacValueSpec.getResourceSpecCharValueUseIds(i));
						rcsorAdFVaSpTrtoRscPK.setAfvsIdAdFacValueSpec(idInsert.longValue());
						RcsorAdFVaSpTrtoRsc.setId(rcsorAdFVaSpTrtoRscPK);
						RcsorAdFVaSpTrtoRsc.setUserIdCreatorParty(usuario);
						RcsorAdFVaSpTrtoRsc.setAudiTiCreation(fechaActual);
						listaRcsorAdFVaSpTrtoRsc.add(RcsorAdFVaSpTrtoRsc);
					}

					// esto para el 61
					for (int i = 0; i < adapFacValueSpec.getAdapNetworkServiceSpecsLength(); i++) {
						RcsorAdNssHasFacVal RcsorAdNssHasFacVal = new RcsorAdNssHasFacVal();
						RcsorAdNssHasFacValPK rcsorAdNssHasFacValPK = new RcsorAdNssHasFacValPK();
						RcsorAdNssHasFacVal.setRcsopAdFacValueSpec(RcsopAdFacValueSpec);
						rcsorAdNssHasFacValPK.setAfvsIdAdFacValueSpec(idInsert.longValue());
						rcsorAdNssHasFacValPK
								.setAnssIdAdapCode(adapFacValueSpec.getAdapNetworkServiceSpecs()[i].getAdapCode());
						RcsorAdNssHasFacVal.setId(rcsorAdNssHasFacValPK);
						RcsorAdNssHasFacVal.setUserIdCreatorParty(usuario);
						RcsorAdNssHasFacVal.setAudiTiCreation(fechaActual);
						listaRcsorAdNssHasFacVal.add(RcsorAdNssHasFacVal);
					}

				}

			}

			// este para el 33
			for (int i = 0; i < adapFacilitySpec.getServiceSpecCharacteristicIdsLength(); i++) {
				RcsorAdFacSpTrSeSCv rcsorAdFacSpTrSeSCv = new RcsorAdFacSpTrSeSCv();
				RcsorAdFacSpTrSeSCvPK rcsorAdFacSpTrSeSCvPK = new RcsorAdFacSpTrSeSCvPK();
				String key = adapFacilitySpec.getAdapCode();
				if (!fix33.contains(key)) {
					fix33.add(key);
					rcsorAdFacSpTrSeSCvPK.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
					rcsorAdFacSpTrSeSCvPK.setSschIdServiceSpecChar(adapFacilitySpec.getServiceSpecCharacteristicIds(i));
					rcsorAdFacSpTrSeSCv.setId(rcsorAdFacSpTrSeSCvPK);
					rcsorAdFacSpTrSeSCv.setUserIdCreatorParty(usuario);
					rcsorAdFacSpTrSeSCv.setAudiTiCreation(fechaActual);
					listaRcsorAdFacSpTrSeSCv.add(rcsorAdFacSpTrSeSCv);
				}
			}

			// este para el 34
			for (int i = 0; i < adapFacilitySpec.getEntityIdentificationSpecificationIdsLength(); i++) {
				String key = adapFacilitySpec.getAdapCode()
						+ adapFacilitySpec.getEntityIdentificationSpecificationIds(i);
				if (!fix34.contains(key)) {
					fix34.add(key);
					RcsorAdFacSpTrtoEi rcsorAdFacSpTrtoEi = new RcsorAdFacSpTrtoEi();
					RcsorAdFacSpTrtoEiPK rcsorAdFacSpTrtoEiPK = new RcsorAdFacSpTrtoEiPK();
					rcsorAdFacSpTrtoEiPK.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
					rcsorAdFacSpTrtoEiPK
							.setReisIdResourceIdentSpec(adapFacilitySpec.getEntityIdentificationSpecificationIds(i));
					rcsorAdFacSpTrtoEi.setId(rcsorAdFacSpTrtoEiPK);
					rcsorAdFacSpTrtoEi.setRcsopAdapFacSpec(rcsopAdapFacSpec);
					rcsorAdFacSpTrtoEi.setUserIdCreatorParty(usuario);
					rcsorAdFacSpTrtoEi.setAudiTiCreation(fechaActual);
					listaRcsorAdFacSpTrtoEi.add(rcsorAdFacSpTrtoEi);
				}
			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapFacilitySpecRel_DTO_IN adapFacilitySpecRel : adapFacilitySpec
					.getAdapFacilitySpecRels()) {
				// este para el 37

				String afsp1 = adapFacilitySpec.getAdapCode();
				if (adapFacilitySpecRel.getAdapFacilitySpec() != null) {
					String afsp2 = adapFacilitySpecRel.getAdapFacilitySpec().getAdapCode();
					String key = afsp1 + afsp2;
					if (!fix37.contains(key)) {
						fix37.add(key);
						RcsorAdapFacSpRel rcsorAdapFacSpRel = new RcsorAdapFacSpRel();
						RcsopAdapFacSpec rcsopAdapFacSpecOr = new RcsopAdapFacSpec();
						rcsopAdapFacSpecOr.setAfspIdAdapCode(adapFacilitySpec.getAdapCode());
						rcsorAdapFacSpRel.setRcsopAdapFacSpec1(rcsopAdapFacSpecOr);
						RcsopAdapFacSpec rcsopAdapFacSpecDe = new RcsopAdapFacSpec();
						rcsopAdapFacSpecDe.setAfspIdAdapCode(adapFacilitySpecRel.getAdapFacilitySpec().getAdapCode());
						rcsorAdapFacSpRel.setRcsopAdapFacSpec2(rcsopAdapFacSpecDe);
						rcsorAdapFacSpRel.setUserIdCreatorParty(usuario);
						rcsorAdapFacSpRel.setAudiTiCreation(fechaActual);
						rcsorAdapFacSpRel.setAfsrQuMaxCardinality(adapFacilitySpecRel.getMaxCardinality());
						rcsorAdapFacSpRel.setAfsrQuMinCardinality(adapFacilitySpecRel.getMinCardinality());
						rcsorAdapFacSpRel.setAfsrNuOrder(adapFacilitySpecRel.getOrder());
						listaRcsorAdapFacSpRel.add(rcsorAdapFacSpRel);
					}
				}
			}
		}
	}

	private void obtenAdapAccesSpec(List<RcsopAdapAccSpec> listaRcsopAdapAccSpec,
			List<RcsorAdAcSpURSpChVal> listaRcsorAdAcSpURSpChVal, List<RcsorAdAccSpULogRSp> listaRcsorAdAccSpULogRSp,
			List<RcsorAdAccUseEqRoSp> listaRcsorAdAccUseEqRoSp, List<RcsorAdAccUseFacSpec> listaRcsorAdAccUseFacSpec,
			Timestamp fechaActual, BigDecimal usuario,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			List<String> fix38, List<String> fix27, List<String> fix23, List<String> fix29) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapAccessSpec_DTO_IN adapAccesSpec : adapNetworkLayerSpec
				.getAdapAccessSpecs()) {
			// este for para el 23 (por el camino mas corto posible) TABLA
			// BASICA

			RcsopAdapAccSpec rcsopAdapAccSpec = new RcsopAdapAccSpec();
			rcsopAdapAccSpec.setAacsIdAdapCode(adapAccesSpec.getAdapCode());
			rcsopAdapAccSpec.setAacsNaMnemonic(adapAccesSpec.getMnemonic());
			rcsopAdapAccSpec.setUserIdCreatorParty(usuario);
			rcsopAdapAccSpec.setAudiTiCreation(fechaActual);
			if (!fix23.contains(adapAccesSpec.getAdapCode())) {
				fix23.add(adapAccesSpec.getAdapCode());
				listaRcsopAdapAccSpec.add(rcsopAdapAccSpec);
			}

			// este para el 24
			for (int i = 0; i < adapAccesSpec.getResourceSpecCharValueUseIdsLength(); i++) {
				RcsorAdAcSpURSpChVal rcsorAdAcSpURSpChVal = new RcsorAdAcSpURSpChVal();
				RcsorAdAcSpURSpChValPK rcsorAdAcSpURSpChValPK = new RcsorAdAcSpURSpChValPK();
				rcsorAdAcSpURSpChValPK.setAacsIdAdapCode(adapAccesSpec.getAdapCode());
				rcsorAdAcSpURSpChValPK.setRsvuIdResSpCharValueUse(adapAccesSpec.getResourceSpecCharValueUseIds(i));
				rcsorAdAcSpURSpChVal.setId(rcsorAdAcSpURSpChValPK);
				rcsorAdAcSpURSpChVal.setUserIdCreatorParty(usuario);
				rcsorAdAcSpURSpChVal.setAudiTiCreation(fechaActual);
				listaRcsorAdAcSpURSpChVal.add(rcsorAdAcSpURSpChVal);
			}

			// este para el 25
			for (int i = 0; i < adapAccesSpec.getLogicalResourceSpecCompositeIdsLength(); i++) {
				RcsorAdAccSpULogRSp rcsorAdAccSpULogRSp = new RcsorAdAccSpULogRSp();
				RcsorAdAccSpULogRSpPK rcsorAdAccSpULogRSpPK = new RcsorAdAccSpULogRSpPK();
				rcsorAdAccSpULogRSpPK.setAacsIdAdapCode(adapAccesSpec.getAdapCode());
				rcsorAdAccSpULogRSpPK.setRsspIdResourceSpec(adapAccesSpec.getLogicalResourceSpecCompositeIds(i));
				rcsorAdAccSpULogRSp.setId(rcsorAdAccSpULogRSpPK);
				rcsorAdAccSpULogRSp.setUserIdCreatorParty(usuario);
				rcsorAdAccSpULogRSp.setAudiTiCreation(fechaActual);
				listaRcsorAdAccSpULogRSp.add(rcsorAdAccSpULogRSp);

			}

			// este para el 27
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapAccessUseEquipRoleSp_DTO_IN adapAccessUseEquipRoleSp : adapAccesSpec
					.getAdapAccessUseEquipRoleSps()) {
				if (adapAccessUseEquipRoleSp != null && adapAccessUseEquipRoleSp.getAdapEquipmentRoleSpec() != null) {
					String aacs = adapAccesSpec.getAdapCode();
					String aers = adapAccessUseEquipRoleSp.getAdapEquipmentRoleSpec().getAdapCode();
					String key = aacs + aers;
					if (!fix27.contains(key)) {
						fix27.add(key);
						RcsorAdAccUseEqRoSp rcsorAdAccUseEqRoSp = new RcsorAdAccUseEqRoSp();
						RcsopAdapAccSpec rcsopAdapAccSpecAux = new RcsopAdapAccSpec();
						rcsopAdapAccSpecAux.setAacsIdAdapCode(adapAccesSpec.getAdapCode());
						rcsorAdAccUseEqRoSp.setRcsopAdapAccSpec(rcsopAdapAccSpecAux);
						RcsopAdapEquipRoleSp rcsopAdapEquipRoleSp = new RcsopAdapEquipRoleSp();
						rcsopAdapEquipRoleSp
								.setAersIdAdapCode(adapAccessUseEquipRoleSp.getAdapEquipmentRoleSpec().getAdapCode());
						rcsorAdAccUseEqRoSp.setRcsopAdapEquipRoleSp(rcsopAdapEquipRoleSp);
						rcsorAdAccUseEqRoSp.setUserIdCreatorParty(usuario);
						rcsorAdAccUseEqRoSp.setAudiTiCreation(fechaActual);
						rcsorAdAccUseEqRoSp.setAaerQuMaxCardinality(adapAccessUseEquipRoleSp.getMaxCardinality());
						rcsorAdAccUseEqRoSp.setAaerQuMinCardinality(adapAccessUseEquipRoleSp.getMinCardinality());
						listaRcsorAdAccUseEqRoSp.add(rcsorAdAccUseEqRoSp);
					}
				}
			}

			// este para el 38
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapAccessUseFacSpec_DTO_IN adapAccessUseFacSpec : adapAccesSpec
					.getAdapAccessUseFacSpecs()) {
				if (adapAccessUseFacSpec != null && adapAccessUseFacSpec.getAdapFacilitySpec() != null
						&& adapAccessUseFacSpec.getAdapFacilitySpec().getAdapFacilitySpecRels() != null) {
					String afsp = adapAccessUseFacSpec.getAdapFacilitySpec().getAdapCode();
					String aacs = rcsopAdapAccSpec.getAacsIdAdapCode();
					String key = afsp + aacs;
					if (!fix38.contains(key)) {
						fix38.add(key);

						RcsorAdAccUseFacSpec rcsorAdAccUseFacSpec = new RcsorAdAccUseFacSpec();
						RcsopAdapFacSpec rcsopAdapFacSpecAux = new RcsopAdapFacSpec();
						rcsopAdapFacSpecAux.setAfspIdAdapCode(adapAccessUseFacSpec.getAdapFacilitySpec().getAdapCode());
						rcsorAdAccUseFacSpec.setRcsopAdapFacSpec(rcsopAdapFacSpecAux);
						RcsopAdapAccSpec rcsopAdapAccSpecAux = new RcsopAdapAccSpec();
						rcsopAdapAccSpecAux.setAacsIdAdapCode(rcsopAdapAccSpec.getAacsIdAdapCode());
						rcsorAdAccUseFacSpec.setRcsopAdapAccSpec(rcsopAdapAccSpecAux);
						rcsorAdAccUseFacSpec.setUserIdCreatorParty(usuario);
						rcsorAdAccUseFacSpec.setAudiTiCreation(fechaActual);
						rcsorAdAccUseFacSpec.setAufsQuMaxCardinality(adapAccessUseFacSpec.getMaxCardinality());
						rcsorAdAccUseFacSpec.setAufsQuMinCardinality(adapAccessUseFacSpec.getMinCardinality());
						listaRcsorAdAccUseFacSpec.add(rcsorAdAccUseFacSpec);
					}
				}
			}
		}
	}

	private void obtenAdapNetworkServiceSpec(List<RcsopAdapNetSerSp> listaRcsopAdapNetSerSp,
			List<RcsodAdNetSeTypeSp> listaRcsodAdNetSeTypeSp, List<RcsorAdNetSSpSuby> listaRcsorAdNetSSpSuby,
			List<RcsorAdNSerTrRfsSp> listaRcsorAdNSerTrRfsSp, List<RcsorAdNetSSPTby> listaRcsorAdNetSSPTby,
			List<RcsorAdNetSSpTbyRec> listaRcsorAdNetSSpTbyRec, List<RcsorAdNSspUseFacSp> listaRcsorAdNSspUseFacSp,
			List<RcsorAdapNetSeSpRel> listaRcsorAdapNetSeSpRel, List<RcsorANetSeHEquRoSp> listaRcsorANetSeHEquRoSp,
			List<RcsodAdNetSerSpReTy> listaRcsodAdNetSerSpReTy, List<RcsoaAdTaskStepSp> listaRcsoaAdTaskStepSp,
			List<RcsorAdNSeReqTrailSp> listaRcsorAdNSeReqTrailSp, List<RcsorAdapNssIsTrigBy> listaRcsorAdapNssIsTrigBy,
			Timestamp fechaActual, BigDecimal usuario, Timestamp fecha_default_end,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			Set<Long> setAdNetSeTypeSp, List<String> fix48, List<String> fix15, List<String> fix20, List<String> fix29,
			List<String> fix10, List<String> fix17) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkServiceSpec_DTO_IN adapNetworkServiceSpec : adapNetworkLayerSpec
				.getAdapNetworkServiceSpecs()) {

			// este para el 10 (por el camino mas corto posible) TABLA
			// BASICA
			if (!setAdNetSeTypeSp.contains(adapNetworkServiceSpec.getAdapNetServTypeSpId())) {
				RcsodAdNetSeTypeSp rcsodAdNetSeTypeSp = new RcsodAdNetSeTypeSp();
				rcsodAdNetSeTypeSp.setAndsIdAdNetSeTypeSp(adapNetworkServiceSpec.getAdapNetServTypeSpId());
				rcsodAdNetSeTypeSp.setAndsNaAdNetSeTypeSp(adapNetworkServiceSpec.getAdapNetServTypeSpName());
				rcsodAdNetSeTypeSp.setAndsTiStartValidity(fechaActual);
				rcsodAdNetSeTypeSp.setAndsTiEndValidity(fecha_default_end);
				rcsodAdNetSeTypeSp.setUserIdCreatorParty(usuario);
				rcsodAdNetSeTypeSp.setAudiTiCreation(fechaActual);
				listaRcsodAdNetSeTypeSp.add(rcsodAdNetSeTypeSp);
				setAdNetSeTypeSp.add(adapNetworkServiceSpec.getAdapNetServTypeSpId());
			}

			// este para el 9
			if (adapNetworkServiceSpec != null && adapNetworkServiceSpec.getAdapCode() != null
					&& !fix10.contains(adapNetworkServiceSpec.getAdapCode())) {
				fix10.add(adapNetworkServiceSpec.getAdapCode());
				RcsopAdapNetSerSp rcsopAdapNetSerSp = new RcsopAdapNetSerSp();
				rcsopAdapNetSerSp.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				RcsodAdNetSeTypeSp rcsodAdNetSeTypeSpAux = new RcsodAdNetSeTypeSp();
				rcsodAdNetSeTypeSpAux.setAndsIdAdNetSeTypeSp(adapNetworkServiceSpec.getAdapNetServTypeSpId());
				rcsopAdapNetSerSp.setAnssNaMnemonic(adapNetworkServiceSpec.getMnemonic());
				rcsopAdapNetSerSp.setRcsodAdNetSeTypeSp(rcsodAdNetSeTypeSpAux);
				rcsopAdapNetSerSp.setUserIdCreatorParty(usuario);
				rcsopAdapNetSerSp.setAudiTiCreation(fechaActual);
				listaRcsopAdapNetSerSp.add(rcsopAdapNetSerSp);
			}

			// este para el 11
			for (int i = 0; i < adapNetworkServiceSpec.getRestrictionSpecIdsLength(); i++) {
				RcsorAdNetSSpSuby rcsorAdNetSSpSuby = new RcsorAdNetSSpSuby();
				RcsorAdNetSSpSubyPK rcsorAdNetSSpSubyPK = new RcsorAdNetSSpSubyPK();
				rcsorAdNetSSpSubyPK.setBrspIdRestrictionSpec(adapNetworkServiceSpec.getRestrictionSpecIds(i));
				rcsorAdNetSSpSubyPK.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				rcsorAdNetSSpSuby.setId(rcsorAdNetSSpSubyPK);
				rcsorAdNetSSpSuby.setUserIdCreatorParty(usuario);
				rcsorAdNetSSpSuby.setAudiTiCreation(fechaActual);
				listaRcsorAdNetSSpSuby.add(rcsorAdNetSSpSuby);
			}

			// este para el 12
			for (int i = 0; i < adapNetworkServiceSpec.getResourceFacingServiceSpecAtomicIdsLength(); i++) {
				RcsorAdNSerTrRfsSp rcsorAdNSerTrRfsSp = new RcsorAdNSerTrRfsSp();
				RcsorAdNSerTrRfsSpPK rcsorAdNSerTrRfsSpPK = new RcsorAdNSerTrRfsSpPK();
				rcsorAdNSerTrRfsSpPK.setSespIdServSpec(adapNetworkServiceSpec.getResourceFacingServiceSpecAtomicIds(i));
				rcsorAdNSerTrRfsSpPK.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				rcsorAdNSerTrRfsSp.setId(rcsorAdNSerTrRfsSpPK);
				rcsorAdNSerTrRfsSp.setUserIdCreatorParty(usuario);
				rcsorAdNSerTrRfsSp.setAudiTiCreation(fechaActual);
				listaRcsorAdNSerTrRfsSp.add(rcsorAdNSerTrRfsSp);
			}

			// este para el 13
			for (int i = 0; i < adapNetworkServiceSpec.getResourceSpecCharValueUseIdsLength(); i++) {
				RcsorAdNetSSPTby rcsorAdNetSSPTby = new RcsorAdNetSSPTby();
				RcsorAdNetSSPTbyPK rcsorAdNetSSPTbyPK = new RcsorAdNetSSPTbyPK();
				rcsorAdNetSSPTbyPK.setRsvuIdResSpCharValueUse(adapNetworkServiceSpec.getResourceSpecCharValueUseIds(i));
				rcsorAdNetSSPTbyPK.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				rcsorAdNetSSPTby.setId(rcsorAdNetSSPTbyPK);
				rcsorAdNetSSPTby.setUserIdCreatorParty(usuario);
				rcsorAdNetSSPTby.setAudiTiCreation(fechaActual);
				listaRcsorAdNetSSPTby.add(rcsorAdNetSSPTby);
			}

			// este para el 14
			for (int i = 0; i < adapNetworkServiceSpec.getResourceConfigSpecIdsLength(); i++) {
				RcsorAdNetSSpTbyRec rcsorAdNetSSpTbyRec = new RcsorAdNetSSpTbyRec();
				RcsorAdNetSSpTbyRecPK rcsorAdNetSSpTbyRecPK = new RcsorAdNetSSpTbyRecPK();
				rcsorAdNetSSpTbyRecPK.setCgspIdConfigurationSpec(adapNetworkServiceSpec.getResourceConfigSpecIds(i));
				rcsorAdNetSSpTbyRecPK.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				rcsorAdNetSSpTbyRec.setId(rcsorAdNetSSpTbyRecPK);
				rcsorAdNetSSpTbyRec.setUserIdCreatorParty(usuario);
				rcsorAdNetSSpTbyRec.setAudiTiCreation(fechaActual);
				listaRcsorAdNetSSpTbyRec.add(rcsorAdNetSSpTbyRec);
			}

			// este para el 15
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetSSpUseFacilitySp_DTO_IN adapNetSSpUseFacilitySp : adapNetworkServiceSpec
					.getAdapNetSSpUseFacilitySps()) {
				if (adapNetSSpUseFacilitySp.getAdapFacilitySpec() != null
						&& adapNetSSpUseFacilitySp.getAdapFacilitySpec().getAdapCode() != null) {
					String anss = adapNetworkServiceSpec.getAdapCode();
					String afsp = adapNetSSpUseFacilitySp.getAdapFacilitySpec().getAdapCode();
					String key = anss + afsp;
					if (!fix15.contains(key)) {
						// if (fix29.contains(afsp)) {
						fix15.add(key);
						RcsorAdNSspUseFacSp RcsorAdNSspUseFacSp = new RcsorAdNSspUseFacSp();
						RcsopAdapNetSerSp rcsopAdapNetSerSpAux = new RcsopAdapNetSerSp();
						rcsopAdapNetSerSpAux.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
						RcsorAdNSspUseFacSp.setRcsopAdapNetSerSp(rcsopAdapNetSerSpAux);
						RcsopAdapFacSpec rcsopAdapFacSpec = new RcsopAdapFacSpec();
						rcsopAdapFacSpec.setAfspIdAdapCode(adapNetSSpUseFacilitySp.getAdapFacilitySpec().getAdapCode());
						RcsorAdNSspUseFacSp.setRcsopAdapFacSpec(rcsopAdapFacSpec);
						RcsorAdNSspUseFacSp.setUserIdCreatorParty(usuario);
						RcsorAdNSspUseFacSp.setAudiTiCreation(fechaActual);
						RcsorAdNSspUseFacSp.setNsufQuMaxCardinality(adapNetSSpUseFacilitySp.getMaxCardinality());
						RcsorAdNSspUseFacSp.setNsufQuMinCardinality(adapNetSSpUseFacilitySp.getMinCardinality());
						RcsorAdNSspUseFacSp.setNsufNuOrder(adapNetSSpUseFacilitySp.getOrder());
						listaRcsorAdNSspUseFacSp.add(RcsorAdNSspUseFacSp);
						// }
					}
				}

			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkServiceSpecRel_DTO_IN adapNetworkServiceSpecRel : adapNetworkServiceSpec
					.getAdapNetworkServiceSpecRels()) {

				// este for para el 21 (por el camino mas corto posible)
				// TABLA BASICA
				RcsodAdNetSerSpReTy rcsodAdNetSerSpReTy = new RcsodAdNetSerSpReTy();
				if (adapNetworkServiceSpecRel.getAdapNetwSerSpRelType() != null) {
					rcsodAdNetSerSpReTy
							.setNrytIdAdNetSerSpReTy(adapNetworkServiceSpecRel.getAdapNetwSerSpRelType().getId());
					rcsodAdNetSerSpReTy.setNrytTiStartValidity(fechaActual);
					rcsodAdNetSerSpReTy.setNrytTiEndValidity(fecha_default_end);
					rcsodAdNetSerSpReTy.setUserIdCreatorParty(usuario);
					rcsodAdNetSerSpReTy.setAudiTiCreation(fechaActual);
					rcsodAdNetSerSpReTy
							.setNrytNaAdNetSerSpReTy(adapNetworkServiceSpecRel.getAdapNetwSerSpRelType().getName());
					listaRcsodAdNetSerSpReTy.add(rcsodAdNetSerSpReTy);
				}

				// este para el 17
				RcsorAdapNetSeSpRel rcsorAdapNetSeSpRel = new RcsorAdapNetSeSpRel();
				RcsopAdapNetSerSp rcsopAdapNetSerSpOri = new RcsopAdapNetSerSp();
				rcsopAdapNetSerSpOri.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
				rcsorAdapNetSeSpRel.setRcsopAdapNetSerSp1(rcsopAdapNetSerSpOri);
				RcsopAdapNetSerSp rcsopAdapNetSerSpDest = new RcsopAdapNetSerSp();
				rcsopAdapNetSerSpDest
						.setAnssIdAdapCode(adapNetworkServiceSpecRel.getAdapNetworkServiceSpec().getAdapCode());
				rcsorAdapNetSeSpRel.setRcsopAdapNetSerSp2(rcsopAdapNetSerSpDest);
				rcsorAdapNetSeSpRel.setUserIdCreatorParty(usuario);
				rcsorAdapNetSeSpRel.setAudiTiCreation(fechaActual);
				rcsorAdapNetSeSpRel.setNssrQuMaxCardinality(adapNetworkServiceSpecRel.getMaxCardinality());
				rcsorAdapNetSeSpRel.setNssrQuMinCardinality(adapNetworkServiceSpecRel.getMinCardinality());
				rcsorAdapNetSeSpRel.setNssrNuOrder(adapNetworkServiceSpecRel.getOrder());
				if (adapNetworkServiceSpecRel.getAdapNetwSerSpRelType() != null) {
					RcsodAdNetSerSpReTy rcsodAdNetSerSpReTyAux = new RcsodAdNetSerSpReTy();
					rcsodAdNetSerSpReTyAux
							.setNrytIdAdNetSerSpReTy(adapNetworkServiceSpecRel.getAdapNetwSerSpRelType().getId());
					rcsorAdapNetSeSpRel.setRcsodAdNetSerSpReTy(rcsodAdNetSerSpReTyAux);
				}
				String anss1 = adapNetworkServiceSpec.getAdapCode();
				String anss2 = adapNetworkServiceSpecRel.getAdapNetworkServiceSpec().getAdapCode();
				String key = anss1 + anss2;
				if (!fix17.contains(key)) {
					fix17.add(key);
					listaRcsorAdapNetSeSpRel.add(rcsorAdapNetSeSpRel);
				}
			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapTaskStepSpec_DTO_IN adapTaskStepSpec : adapNetworkServiceSpec
					.getAdapTaskStepSpecs()) {
				if (adapTaskStepSpec != null) {
					// este for para el 22 (por el camino mas corto posible)
					// TABLA BASICA
					RcsoaAdTaskStepSp rcsoaAdTaskStepSp = new RcsoaAdTaskStepSp();
					rcsoaAdTaskStepSp.setAtssIdAdTaskStepSp(adapTaskStepSpec.getId());
					rcsoaAdTaskStepSp.setPtsyIdPoTaskStepsType(adapTaskStepSpec.getPOTaskStepsTypeId());
					rcsoaAdTaskStepSp.setSespIdServSpecRfs(adapTaskStepSpec.getResourceFacingServiceSpecAtomicId());
					rcsoaAdTaskStepSp.setRrosIdResourceRoleSpec(adapTaskStepSpec.getResourceRoleSpecificationId());
					rcsoaAdTaskStepSp.setUserIdCreatorParty(usuario);
					rcsoaAdTaskStepSp.setAudiTiCreation(fechaActual);
					listaRcsoaAdTaskStepSp.add(rcsoaAdTaskStepSp);
				}
			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetServHasEquipmRoleSp_DTO_IN adapNetServHasEquipmRoleSp : adapNetworkServiceSpec
					.getAdapNetServHasEquipmRoleSps()) {
				// este para el 20

				if (adapNetServHasEquipmRoleSp != null
						&& adapNetServHasEquipmRoleSp.getAdapEquipmentRoleSpec() != null) {
					String anss = adapNetworkServiceSpec.getAdapCode();
					String aers = adapNetServHasEquipmRoleSp.getAdapEquipmentRoleSpec().getAdapCode();
					String key = anss + aers;
					if (!fix20.contains(key)) {
						fix20.add(key);
						RcsorANetSeHEquRoSp rcsorANetSeHEquRoSp = new RcsorANetSeHEquRoSp();
						RcsopAdapNetSerSp rcsopAdapNetSerSpAux = new RcsopAdapNetSerSp();
						rcsopAdapNetSerSpAux.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
						rcsorANetSeHEquRoSp.setRcsopAdapNetSerSp(rcsopAdapNetSerSpAux);
						RcsopAdapEquipRoleSp rcsopAdapEquipRoleSp = new RcsopAdapEquipRoleSp();
						rcsopAdapEquipRoleSp
								.setAersIdAdapCode(adapNetServHasEquipmRoleSp.getAdapEquipmentRoleSpec().getAdapCode());
						rcsorANetSeHEquRoSp.setRcsopAdapEquipRoleSp(rcsopAdapEquipRoleSp);
						rcsorANetSeHEquRoSp.setUserIdCreatorParty(usuario);
						rcsorANetSeHEquRoSp.setAudiTiCreation(fechaActual);
						rcsorANetSeHEquRoSp.setAhqrQuMaxCardinality(adapNetServHasEquipmRoleSp.getMaxCardinality());
						rcsorANetSeHEquRoSp.setAhqrQuMinCardinality(adapNetServHasEquipmRoleSp.getMinCardinality());
						listaRcsorANetSeHEquRoSp.add(rcsorANetSeHEquRoSp);
					}
				}

			}

			// este para el 48
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetServRequiresTrailSp_DTO_IN adapNetServRequiresTrailSp : adapNetworkServiceSpec
					.getAdapNetServRequiresTrailSps()) {
				if (adapNetServRequiresTrailSp != null && adapNetServRequiresTrailSp.getAdapTrailSpec() != null) {
					String anss = adapNetworkServiceSpec.getAdapCode();
					String atrr = adapNetServRequiresTrailSp.getAdapTrailSpec().getAdapCode();
					String key = anss + atrr;
					if (!fix48.contains(key)) {
						fix48.add(key);
						RcsorAdNSeReqTrailSp RcsorAdNSeReqTrailSp = new RcsorAdNSeReqTrailSp();
						RcsopAdapNetSerSp rcsopAdapNetSerSpAux = new RcsopAdapNetSerSp();
						rcsopAdapNetSerSpAux.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
						RcsorAdNSeReqTrailSp.setRcsopAdapNetSerSp(rcsopAdapNetSerSpAux);
						RcsopAdapTrailSp rcsopAdapTrailSp = new RcsopAdapTrailSp();
						rcsopAdapTrailSp.setAtrrIdAdapCode(adapNetServRequiresTrailSp.getAdapTrailSpec().getAdapCode());
						RcsorAdNSeReqTrailSp.setRcsopAdapTrailSp(rcsopAdapTrailSp);
						RcsorAdNSeReqTrailSp.setUserIdCreatorParty(usuario);
						RcsorAdNSeReqTrailSp.setAudiTiCreation(fechaActual);
						RcsorAdNSeReqTrailSp.setAsrtQuMaxCardinality(adapNetServRequiresTrailSp.getMaxCardinality());
						RcsorAdNSeReqTrailSp.setAsrtQuMinCardinality(adapNetServRequiresTrailSp.getMinCardinality());
						listaRcsorAdNSeReqTrailSp.add(RcsorAdNSeReqTrailSp);
					}
				}
			}

			// List<RcsorAdapNssIsTrigBy> listaRcsorAdapNssIsTrigBy
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapOperation_DTO_IN adapOperation : adapNetworkServiceSpec
					.getAdapOperations()) {
				if (adapOperation != null && adapOperation.getId() != null) {
					RcsorAdapNssIsTrigBy rcsorAdapNssIsTrigBy = new RcsorAdapNssIsTrigBy();
					RcsorAdapNssIsTrigByPK id = new RcsorAdapNssIsTrigByPK();
					id.setAdopIdAdapOperation(adapOperation.getId());
					id.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
					rcsorAdapNssIsTrigBy.setId(id);
					RcsopAdapNetSerSp rcsopAdapNetSerSp = new RcsopAdapNetSerSp();
					rcsopAdapNetSerSp.setAnssIdAdapCode(adapNetworkServiceSpec.getAdapCode());
					rcsorAdapNssIsTrigBy.setRcsopAdapNetSerSp(rcsopAdapNetSerSp);
					RcsopAdapOperation rcsopAdapOperation = new RcsopAdapOperation();
					rcsopAdapOperation.setAdopIdAdapOperation(adapOperation.getId());
					rcsorAdapNssIsTrigBy.setRcsopAdapOperation(rcsopAdapOperation);
					rcsorAdapNssIsTrigBy.setAudiTiCreation(fechaActual);
					rcsorAdapNssIsTrigBy.setUserIdCreatorParty(usuario);
					listaRcsorAdapNssIsTrigBy.add(rcsorAdapNssIsTrigBy);
				}
			}
		}
	}

	private void obtenAdapServiceLineSpec(List<RcsopAdapSerLineSp> listaRcsopAdapSerLineSp,
			List<RcsorAdapSeLiTrRfsSp> listaRcsorAdapSeLiTrRfsSp,
			List<RcsorAdSeliSpUseNetse> listaRcsorAdSeliSpUseNetse, List<RcsorAdSerLiUseAcSp> listaRcsorAdSerLiUseAcSp,
			List<RcsorAdSlSpUseFaSp> listaRcsorAdSlSpUseFaSp, Timestamp fechaActual, BigDecimal usuario,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			List<String> fix26, List<String> fix16, List<String> fix36,
			List<RcsorNetLayHasSeLine> listaRcsorNetLayHasSeLine) {
		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapServiceLineSpec_DTO_IN adapServiceLineSpec : adapNetworkLayerSpec
				.getAdapServiceLineSpecs()) {
			// este para el 7 (por el camino mas corto posible) TABLA BASICA
			RcsopAdapSerLineSp rcsopAdapSerLineSp = new RcsopAdapSerLineSp();
			rcsopAdapSerLineSp.setAslsIdAdapCode(adapServiceLineSpec.getAdapCode());
			rcsopAdapSerLineSp.setAslsNaMnemonic(adapServiceLineSpec.getMnemonic());
			rcsopAdapSerLineSp.setUserIdCreatorParty(usuario);
			rcsopAdapSerLineSp.setAudiTiCreation(fechaActual);
			listaRcsopAdapSerLineSp.add(rcsopAdapSerLineSp);

			// este para el 8
			for (int i = 0; i < adapServiceLineSpec.getResourceFacingServiceSpecAtomicIdsLength(); i++) {
				RcsorAdapSeLiTrRfsSp rcsorAdapSeLiTrRfsSp = new RcsorAdapSeLiTrRfsSp();
				RcsorAdapSeLiTrRfsSpPK rcsorAdapSeLiTrRfsSpPK = new RcsorAdapSeLiTrRfsSpPK();
				rcsorAdapSeLiTrRfsSpPK.setAslsIdAdapCode(rcsopAdapSerLineSp.getAslsIdAdapCode());
				rcsorAdapSeLiTrRfsSpPK.setSespIdServSpec(adapServiceLineSpec.getResourceFacingServiceSpecAtomicIds(i));
				rcsorAdapSeLiTrRfsSp.setId(rcsorAdapSeLiTrRfsSpPK);
				rcsorAdapSeLiTrRfsSp.setUserIdCreatorParty(usuario);
				rcsorAdapSeLiTrRfsSp.setAudiTiCreation(fechaActual);
				listaRcsorAdapSeLiTrRfsSp.add(rcsorAdapSeLiTrRfsSp);
			}

			// este para el 36
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapSLSpUseFacilitySp_DTO_IN adapSLSpUseFacilitySp : adapServiceLineSpec
					.getAdapSLSpUseFacilitySps()) {
				if (adapSLSpUseFacilitySp.getAdapFacilitySpec() != null) {
					String asls = adapServiceLineSpec.getAdapCode();
					String afsp = adapSLSpUseFacilitySp.getAdapFacilitySpec().getAdapCode();
					String key = asls + afsp;
					if (!fix36.contains(key)) {
						fix36.add(key);
						RcsorAdSlSpUseFaSp rcsorAdSlSpUseFaSp = new RcsorAdSlSpUseFaSp();
						RcsopAdapSerLineSp rcsopAdapSerLineSpAux = new RcsopAdapSerLineSp();
						rcsopAdapSerLineSpAux.setAslsIdAdapCode(adapServiceLineSpec.getAdapCode());
						rcsorAdSlSpUseFaSp.setRcsopAdapSerLineSp(rcsopAdapSerLineSpAux);
						RcsopAdapFacSpec rcsopAdapFacSpec = new RcsopAdapFacSpec();
						rcsopAdapFacSpec.setAfspIdAdapCode(adapSLSpUseFacilitySp.getAdapFacilitySpec().getAdapCode());
						rcsorAdSlSpUseFaSp.setRcsopAdapFacSpec(rcsopAdapFacSpec);
						rcsorAdSlSpUseFaSp.setAslfQuMaxCardinality(adapSLSpUseFacilitySp.getMaxCardinality());
						rcsorAdSlSpUseFaSp.setAslfQuMinCardinality(adapSLSpUseFacilitySp.getMinCardinality());
						rcsorAdSlSpUseFaSp.setUserIdCreatorParty(usuario);
						rcsorAdSlSpUseFaSp.setAudiTiCreation(fechaActual);
						listaRcsorAdSlSpUseFaSp.add(rcsorAdSlSpUseFaSp);
					}
				}
			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapServLineSpUseNetServ_DTO_IN adapServLineSpUseNetServ : adapServiceLineSpec
					.getAdapServLineSpUseNetServs()) {
				// este para el 16
				if (adapServLineSpUseNetServ.getAdapNetworkServiceSpec() != null) {
					String asls = adapServiceLineSpec.getAdapCode();
					String anss = adapServLineSpUseNetServ.getAdapNetworkServiceSpec().getAdapCode();
					String key = asls + anss;
					if (!fix16.contains(key)) {
						fix16.add(key);
						RcsorAdSeliSpUseNetse rcsorAdSeliSpUseNetse = new RcsorAdSeliSpUseNetse();
						RcsopAdapSerLineSp rcsopAdapSerLineSpAux = new RcsopAdapSerLineSp();
						rcsopAdapSerLineSpAux.setAslsIdAdapCode(adapServiceLineSpec.getAdapCode());
						rcsorAdSeliSpUseNetse.setRcsopAdapSerLineSp(rcsopAdapSerLineSpAux);

						RcsopAdapNetSerSp rcsopAdapNetSerSp = new RcsopAdapNetSerSp();
						rcsopAdapNetSerSp
								.setAnssIdAdapCode(adapServLineSpUseNetServ.getAdapNetworkServiceSpec().getAdapCode());
						rcsorAdSeliSpUseNetse.setRcsopAdapNetSerSp(rcsopAdapNetSerSp);

						rcsorAdSeliSpUseNetse.setUserIdCreatorParty(usuario);
						rcsorAdSeliSpUseNetse.setAudiTiCreation(fechaActual);
						rcsorAdSeliSpUseNetse.setAsnuQuMaxCardinality(adapServLineSpUseNetServ.getMaxCardinality());
						rcsorAdSeliSpUseNetse.setAsnuQuMinCardinality(adapServLineSpUseNetServ.getMinCardinality());
						listaRcsorAdSeliSpUseNetse.add(rcsorAdSeliSpUseNetse);
					}
				}
			}

			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapSerLineUseAccessSp_DTO_IN adapSerLineUseAccessSp : adapServiceLineSpec
					.getAdapSerLineUseAccessSps()) {
				if (adapSerLineUseAccessSp != null && adapSerLineUseAccessSp.getAdapAccessSpec() != null) {
					String asls = adapServiceLineSpec.getAdapCode();
					String aacs = adapSerLineUseAccessSp.getAdapAccessSpec().getAdapCode();
					String key = asls + aacs;
					if (!fix26.contains(key)) {
						fix26.add(key);
						// este para el 26
						RcsorAdSerLiUseAcSp rcsorAdSerLiUseAcSp = new RcsorAdSerLiUseAcSp();
						RcsopAdapSerLineSp rcsopAdapSerLineSpAux = new RcsopAdapSerLineSp();
						rcsopAdapSerLineSpAux.setAslsIdAdapCode(adapServiceLineSpec.getAdapCode());
						rcsorAdSerLiUseAcSp.setRcsopAdapSerLineSp(rcsopAdapSerLineSpAux);

						RcsopAdapAccSpec rcsopAdapAccSpecAux = new RcsopAdapAccSpec();
						rcsopAdapAccSpecAux.setAacsIdAdapCode(adapSerLineUseAccessSp.getAdapAccessSpec().getAdapCode());
						rcsorAdSerLiUseAcSp.setRcsopAdapAccSpec(rcsopAdapAccSpecAux);

						rcsorAdSerLiUseAcSp.setUserIdCreatorParty(usuario);
						rcsorAdSerLiUseAcSp.setAudiTiCreation(fechaActual);
						rcsorAdSerLiUseAcSp.setSluaQuMaxCardinality(adapSerLineUseAccessSp.getMaxCardinality());
						rcsorAdSerLiUseAcSp.setSluaQuMinCardinality(adapSerLineUseAccessSp.getMinCardinality());
						listaRcsorAdSerLiUseAcSp.add(rcsorAdSerLiUseAcSp);
					}
				}
			}
			// este para el 55
			String codeNetworkLayerSpec = adapNetworkLayerSpec.getAdapCode();
			String codeAccessSpec = adapServiceLineSpec.getAdapCode();
			RcsorNetLayHasSeLinePK id = new RcsorNetLayHasSeLinePK();
			id.setAnlsIdAdapCode(codeNetworkLayerSpec);
			id.setAslsIdAdapCode(codeAccessSpec);
			RcsorNetLayHasSeLine aux = new RcsorNetLayHasSeLine();
			aux.setId(id);
			aux.setAudiTiCreation(fechaActual);
			aux.setUserIdCreatorParty(usuario);
			listaRcsorNetLayHasSeLine.add(aux);

		}
	}

	private void obtenAdapNetLaySpExecutesOp(List<RcsodAdapOperationType> listaRcsodAdapOperationType,
			List<RcsopAdapOperation> listaRcsopAdapOperation, List<RcsoaAdOpTran> listaRcsoaAdOpTran,
			List<RcsodLockVoiceType> listaRcsodLockVoiceType, List<RcsorAdNetLaySpExeOp> listaRcsorAdNetLaySpExeOp,
			Timestamp fechaActual, BigDecimal usuario, Timestamp fecha_default_end,
			com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetworkLayerSpec_DTO_IN adapNetworkLayerSpec,
			RcsopAdNetLayerSp rcsopAdNetLayerSp, Set<Long> setLockVoiceType, Set<Long> setAdapOperationType,
			List<String> fix6) throws TE_Excepcion {

		for (com.telefonica.gdre.srv.nuc.adapmodel.msg.insertwholeadaptationmodel.AdapNetLaySpExecutesOp_DTO_IN adapNetLaySpExecutesOp : adapNetworkLayerSpec
				.getAdapNetLaySpExecutesOps()) {
			// este para el 2 (por el camino mas corto posible) TABLA BASICA
			// si ya existe se actualiza, si no se inserta
			RcsodAdapOperationType rcsodAdapOperationType = null;
			if (adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeId() != null && !setAdapOperationType
					.contains(adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeId())) {
				try {
					rcsodAdapOperationType = rcsodAdapOperationTypeRepository
							.findOne(adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeId());
				} catch (PersistenceException e) {
					LOGGER.error(e.getMessage());
					throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
				}

				if (rcsodAdapOperationType == null) {
					rcsodAdapOperationType = new RcsodAdapOperationType();
					rcsodAdapOperationType.setAoptIdAdapOperationType(
							adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeId());
				}
				rcsodAdapOperationType.setAoptNaAdapOperationType(
						adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeName());
				rcsodAdapOperationType.setAoptTiStartValidity(fechaActual);
				rcsodAdapOperationType.setAoptTiEndValidity(fecha_default_end);
				rcsodAdapOperationType.setUserIdCreatorParty(usuario);
				rcsodAdapOperationType.setAudiTiCreation(fechaActual);
				listaRcsodAdapOperationType.add(rcsodAdapOperationType);
				setAdapOperationType.add(adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeId());
			}

			// este para el 3 (por el camino mas corto posible)
			// si ya existe se actualiza, si no se inserta
			RcsopAdapOperation rcsopAdapOperation;
			try {
				rcsopAdapOperation = rcsopAdapOperationRepository
						.findOne(adapNetLaySpExecutesOp.getAdapOperation().getId());
			} catch (PersistenceException e) {
				LOGGER.error(e.getMessage());
				throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
			}

			if (rcsopAdapOperation == null) {
				rcsopAdapOperation = new RcsopAdapOperation();
				rcsopAdapOperation.setAdopIdAdapOperation(adapNetLaySpExecutesOp.getAdapOperation().getId());

				rcsopAdapOperation.setAdopIdAdapCode(adapNetLaySpExecutesOp.getAdapOperation().getAdapCode());
				if (adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeId() != null) {
					RcsodAdapOperationType rcsodAdapOperationTypeAux = new RcsodAdapOperationType();
					rcsodAdapOperationTypeAux.setAoptIdAdapOperationType(
							adapNetLaySpExecutesOp.getAdapOperation().getAdapOperationTypeId());
					rcsopAdapOperation.setRcsodAdapOperationType(rcsodAdapOperationTypeAux);
				}
				rcsopAdapOperation.setAdopTiStartValidity(fechaActual);
				rcsopAdapOperation.setAdopTiEndValidity(fecha_default_end);
				rcsopAdapOperation.setUserIdCreatorParty(usuario);
				rcsopAdapOperation.setAudiTiCreation(fechaActual);
				listaRcsopAdapOperation.add(rcsopAdapOperation);

			}

			// este para el 4 (por el camino mas corto posible)
			for (int i = 0; i < adapNetLaySpExecutesOp.getAdapOperation().getItemWorkflowSpecTaskIdsLength(); i++) {
				RcsoaAdOpTran RcsoaAdOpTran = new RcsoaAdOpTran();
				RcsoaAdOpTranPK rcsoaAdOpTranPK = new RcsoaAdOpTranPK();
				rcsoaAdOpTranPK.setIwstIdItemWflowSpTask(
						adapNetLaySpExecutesOp.getAdapOperation().getItemWorkflowSpecTaskIds(i));
				rcsoaAdOpTranPK.setAdopIdAdapOperation(rcsopAdapOperation.getAdopIdAdapOperation());
				RcsoaAdOpTran.setId(rcsoaAdOpTranPK);
				RcsoaAdOpTran.setUserIdCreatorParty(usuario);
				RcsoaAdOpTran.setAudiTiCreation(fechaActual);
				listaRcsoaAdOpTran.add(RcsoaAdOpTran);
			}
			// este para el 5 (por el camino mas corto posible) TABLA BASICA
			if (adapNetLaySpExecutesOp.getLockVoiceTypeId() != null
					&& !setLockVoiceType.contains(adapNetLaySpExecutesOp.getLockVoiceTypeId())) {
				RcsodLockVoiceType rcsodLockVoiceType = new RcsodLockVoiceType();
				rcsodLockVoiceType.setLvotIdLockVoiceType(adapNetLaySpExecutesOp.getLockVoiceTypeId());
				rcsodLockVoiceType.setLvotTiStartValidity(fechaActual);
				rcsodLockVoiceType.setLvotTiEndValidity(fecha_default_end);
				rcsodLockVoiceType.setUserIdCreatorParty(usuario);
				rcsodLockVoiceType.setAudiTiCreation(fechaActual);
				rcsodLockVoiceType.setLvotNaLockVoiceType(adapNetLaySpExecutesOp.getLockVoiceTypeName());
				listaRcsodLockVoiceType.add(rcsodLockVoiceType);
				setLockVoiceType.add(adapNetLaySpExecutesOp.getLockVoiceTypeId());
			}

			// este para el 6
			String adop = rcsopAdapOperation.getAdopIdAdapOperation().toString();
			String anls = rcsopAdNetLayerSp.getAnlsIdAdapCode();
			String key = adop + anls;
			List<Long> listOp = new ArrayList<Long>();
			listOp.add(rcsopAdapOperation.getAdopIdAdapOperation());
			List<RcsorAdNetLaySpExeOp> listRcsorAdNetLaySpExeOpBBDD = rcsorAdNetLaySpExeOpRepository
					.getAdNetLaySpExeOpByIdAdapOperation(listOp);
			if (listRcsorAdNetLaySpExeOpBBDD != null && !listRcsorAdNetLaySpExeOpBBDD.isEmpty()) {
				for (RcsorAdNetLaySpExeOp r : listRcsorAdNetLaySpExeOpBBDD) {
					String anlsToCheck = r.getRcsopAdNetLayerSp().getAnlsIdAdapCode();
					if (anls.equals(anlsToCheck)) {
						fix6.add(key);
					}
				}
			}
			if (!fix6.contains(key)) {
				fix6.add(key);
				RcsorAdNetLaySpExeOp rcsorAdNetLaySpExeOp = new RcsorAdNetLaySpExeOp();
				rcsorAdNetLaySpExeOp.setRcsopAdapOperation(rcsopAdapOperation);
				RcsopAdNetLayerSp rcsopAdNetLayerSpAux = new RcsopAdNetLayerSp();
				rcsopAdNetLayerSpAux.setAnlsIdAdapCode(rcsopAdNetLayerSp.getAnlsIdAdapCode());
				rcsorAdNetLaySpExeOp.setRcsopAdNetLayerSp(rcsopAdNetLayerSpAux);
				rcsorAdNetLaySpExeOp.setUserIdCreatorParty(usuario);
				rcsorAdNetLaySpExeOp.setAudiTiCreation(fechaActual);
				if (adapNetLaySpExecutesOp.getLockVoiceTypeId() != null) {
					RcsodLockVoiceType rcsodLockVoiceTypeAux = new RcsodLockVoiceType();
					rcsodLockVoiceTypeAux.setLvotIdLockVoiceType(adapNetLaySpExecutesOp.getLockVoiceTypeId());
					rcsorAdNetLaySpExeOp.setRcsodLockVoiceType(rcsodLockVoiceTypeAux);
				}
				rcsorAdNetLaySpExeOp.setNleoInPrefulfillment(adapNetLaySpExecutesOp.isPreFulfillment());
				listaRcsorAdNetLaySpExeOp.add(rcsorAdNetLaySpExeOp);
			}
		}
	}

	private com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT[] obtenAdapFacValueSpec(
			MapasCache mapasCache, String rcsopAdapFacSpec) {

		List<RcsopAdFacValueSpec> listaRcsopAdFacValueSpec = new ArrayList<RcsopAdFacValueSpec>();

		Map<String, List<RcsopAdFacValueSpec>> mapaAdFacValueSpec = mapasCache.getMapaAdFacValueSpec();

		listaRcsopAdFacValueSpec = mapaAdFacValueSpec.get(rcsopAdapFacSpec);

		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT[] arrayAdapFacValueSpec = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT[0];

		if (null != listaRcsopAdFacValueSpec) {
			arrayAdapFacValueSpec = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT[listaRcsopAdFacValueSpec
					.size()];
			for (int indice = 0; indice < arrayAdapFacValueSpec.length; indice++) {
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT adapFacValueSpec = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacValueSpec_DTO_OUT();
				adapFacValueSpec.setAdapCode(listaRcsopAdFacValueSpec.get(indice).getAfvsIdAdapCode());

				adapFacValueSpec.setAdapNetworkServiceSpecs(obtenAdapNetworkServiceSpecs(mapasCache,
						listaRcsopAdFacValueSpec.get(indice).getAfvsIdAdFacValueSpec()));
				adapFacValueSpec.setGeographicAreaIds(obtenGeographicAreaIdsByAdapFacValueSpecId(mapasCache,
						listaRcsopAdFacValueSpec.get(indice).getAfvsIdAdFacValueSpec()));
				adapFacValueSpec.setProductSpecCharacteristicValueIds(
						obtenProductSpecCharacteristicValueIdsByAdapCodeId(mapasCache,
								listaRcsopAdFacValueSpec.get(indice).getAfvsIdAdapCode()));
				adapFacValueSpec.setResourceSpecCharValueUseIds(obtenResourceSpecCharValueUseIdsByAdapFacValueSpecId(
						mapasCache, listaRcsopAdFacValueSpec.get(indice).getAfvsIdAdFacValueSpec()));
				adapFacValueSpec.setRestrictionSpecIds(obtenRestrictionSpecIdsByAdapCodeId(mapasCache,
						listaRcsopAdFacValueSpec.get(indice).getAfvsIdAdapCode()));
				adapFacValueSpec.setServiceSpecCharValueUseIds(obtenServiceSpecCharValueUseIdsByAdapFacValueSpecId(
						mapasCache, listaRcsopAdFacValueSpec.get(indice).getAfvsIdAdFacValueSpec()));
				arrayAdapFacValueSpec[indice] = adapFacValueSpec;
			}
		}

		return arrayAdapFacValueSpec;

	}

	private com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT[] obtenAdapNetworkServiceSpecs(
			MapasCache mapasCache, Long rcsopAdFacValueSpec) {

		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT[] arrayAdapNetworkServiceSpec = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT[0];

		if (null != mapasCache.getMapaAdNssHasFacVal().get(rcsopAdFacValueSpec)) {
			List<RcsorAdNssHasFacVal> listaRcsorAdFVaSpTrtoGa = mapasCache.getMapaAdNssHasFacVal()
					.get(rcsopAdFacValueSpec);
			arrayAdapNetworkServiceSpec = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT[listaRcsorAdFVaSpTrtoGa
					.size()];
			for (int indice = 0; indice < listaRcsorAdFVaSpTrtoGa.size(); indice++) {
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT adapNetworkServiceSpecDTOOUT = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapNetworkServiceSpec_DTO_OUT();
				if (null == mapasCache.getMapaAdapNetworkServiceSpecDTO()
						.get(listaRcsorAdFVaSpTrtoGa.get(indice).getId().getAnssIdAdapCode())) {

					// adapNetworkServiceSpecDTOOUT =
					// creaAdapNetworkServiceSpecDTO(mapasCache,
					// listaRcsorAdFVaSpTrtoGa.get(indice).getId().getAnssIdAdapCode());

				} else {

					adapNetworkServiceSpecDTOOUT = mapasCache.getMapaAdapNetworkServiceSpecDTO()
							.get(listaRcsorAdFVaSpTrtoGa.get(indice).getId().getAnssIdAdapCode());

				}

				arrayAdapNetworkServiceSpec[indice] = adapNetworkServiceSpecDTOOUT;
			}
		}

		return arrayAdapNetworkServiceSpec;

	}

	private Long[] obtenGeographicAreaIdsByAdapFacValueSpecId(MapasCache mapasCache, Long adapFacValueSpecId) {
		List<RcsorAdFVaSpTrtoGa> listaRcsorAdFVaSpTrtoGa = new ArrayList<RcsorAdFVaSpTrtoGa>();
		Map<Long, List<RcsorAdFVaSpTrtoGa>> mapaAdFVaSpTrtoGa = mapasCache.getMapaAdFVaSpTrtoGa();
		Long[] arrayGeographicAreaId = new Long[0];
		listaRcsorAdFVaSpTrtoGa = mapaAdFVaSpTrtoGa.get(adapFacValueSpecId);
		if (null != listaRcsorAdFVaSpTrtoGa) {
			arrayGeographicAreaId = new Long[listaRcsorAdFVaSpTrtoGa.size()];
			for (int indice = 0; indice < arrayGeographicAreaId.length; indice++) {
				Long geographicAreaId = listaRcsorAdFVaSpTrtoGa.get(indice).getId().getPlacIdGeographicArea();
				arrayGeographicAreaId[indice] = geographicAreaId;
			}
		}
		return arrayGeographicAreaId;

	}

	private Long[] obtenProductSpecCharacteristicValueIdsByAdapCodeId(MapasCache mapasCache, String afvsIdAdapCode) {
		List<RcsorAdFaPsChVa> listaRcsorAdFaPsChVa = new ArrayList<RcsorAdFaPsChVa>();
		Map<String, List<RcsorAdFaPsChVa>> mapaAdFaPsChVa = mapasCache.getMapaAdFaPsChVa();
		Long[] arrayProductSpecCharacteristicValueIds = new Long[0];
		listaRcsorAdFaPsChVa = mapaAdFaPsChVa.get(afvsIdAdapCode);
		if (null != listaRcsorAdFaPsChVa) {
			arrayProductSpecCharacteristicValueIds = new Long[listaRcsorAdFaPsChVa.size()];
			for (int indice = 0; indice < arrayProductSpecCharacteristicValueIds.length; indice++) {
				Long productSpecCharacteristicValueIds = listaRcsorAdFaPsChVa.get(indice).getId()
						.getPsvaIdProdSpecCharVal();
				arrayProductSpecCharacteristicValueIds[indice] = productSpecCharacteristicValueIds;
			}
		}
		return arrayProductSpecCharacteristicValueIds;

	}

	private Long[] obtenResourceSpecCharValueUseIdsByAdapFacValueSpecId(MapasCache mapasCache,
			Long adapFacValueSpecId) {
		List<RcsorAdFVaSpTrtoRsc> listaRcsorAdFVaSpTrtoRsc = new ArrayList<RcsorAdFVaSpTrtoRsc>();
		Map<Long, List<RcsorAdFVaSpTrtoRsc>> mapaAdFVaSpTrtoRsc = mapasCache.getMapaAdFVaSpTrtoRsc();
		Long[] arrayResourceSpecCharValueUseIds = new Long[0];
		listaRcsorAdFVaSpTrtoRsc = mapaAdFVaSpTrtoRsc.get(adapFacValueSpecId);
		if (null != listaRcsorAdFVaSpTrtoRsc) {
			arrayResourceSpecCharValueUseIds = new Long[listaRcsorAdFVaSpTrtoRsc.size()];
			for (int indice = 0; indice < arrayResourceSpecCharValueUseIds.length; indice++) {
				Long resourceSpecCharValueUseIds = listaRcsorAdFVaSpTrtoRsc.get(indice).getId()
						.getRsvuIdResSpCharValueUse();
				arrayResourceSpecCharValueUseIds[indice] = resourceSpecCharValueUseIds;
			}
		}
		return arrayResourceSpecCharValueUseIds;
	}

	private Long[] obtenRestrictionSpecIdsByAdapCodeId(MapasCache mapasCache, String afvsIdAdapCode) {
		List<RcsorAdFaSHResS> listaRcsorAdFaSHResS = new ArrayList<RcsorAdFaSHResS>();
		Map<String, List<RcsorAdFaSHResS>> mapaAdFaSHResS = mapasCache.getMapaAdFaSHResS();
		Long[] arrayRestrictionId = new Long[0];
		listaRcsorAdFaSHResS = mapaAdFaSHResS.get(afvsIdAdapCode);
		if (null != listaRcsorAdFaSHResS) {
			arrayRestrictionId = new Long[listaRcsorAdFaSHResS.size()];
			for (int indice = 0; indice < arrayRestrictionId.length; indice++) {
				Long restrictionId = listaRcsorAdFaSHResS.get(indice).getId().getBrspIdRestrictionSpec();
				arrayRestrictionId[indice] = restrictionId;
			}
		}
		return arrayRestrictionId;
	}

	private Long[] obtenServiceSpecCharValueUseIdsByAdapFacValueSpecId(MapasCache mapasCache, Long adapFacValueSpecId) {
		List<RcsorAdFVaSpTrtoSsc> listaRcsorAdFVaSpTrtoSsc = new ArrayList<RcsorAdFVaSpTrtoSsc>();
		Map<Long, List<RcsorAdFVaSpTrtoSsc>> mapaAdFVaSpTrtoSsc = mapasCache.getMapaAdFVaSpTrtoSsc();
		Long[] arrayServiceSpecCharValueUseIds = new Long[0];
		listaRcsorAdFVaSpTrtoSsc = mapaAdFVaSpTrtoSsc.get(adapFacValueSpecId);
		if (null != listaRcsorAdFVaSpTrtoSsc) {
			arrayServiceSpecCharValueUseIds = new Long[listaRcsorAdFVaSpTrtoSsc.size()];
			for (int indice = 0; indice < arrayServiceSpecCharValueUseIds.length; indice++) {
				Long serviceSpecCharValueUseIds = listaRcsorAdFVaSpTrtoSsc.get(indice).getId()
						.getScvuIdServSpCharValueUse();
				arrayServiceSpecCharValueUseIds[indice] = serviceSpecCharValueUseIds;
			}
		}
		return arrayServiceSpecCharValueUseIds;
	}

	private void realizarInsertsAdaptationModel(List<RcsopAdNetLayerSp> listaRcsopAdNetLayerSp,
			List<RcsodAdapOperationType> listaRcsodAdapOperationType, List<RcsopAdapOperation> listaRcsopAdapOperation,
			List<RcsoaAdOpTran> listaRcsoaAdOpTran, List<RcsodLockVoiceType> listaRcsodLockVoiceType,
			List<RcsorAdNetLaySpExeOp> listaRcsorAdNetLaySpExeOp, List<RcsopAdapSerLineSp> listaRcsopAdapSerLineSp,
			List<RcsorAdapSeLiTrRfsSp> listaRcsorAdapSeLiTrRfsSp, List<RcsopAdapNetSerSp> listaRcsopAdapNetSerSp,
			List<RcsodAdNetSeTypeSp> listaRcsodAdNetSeTypeSp, List<RcsorAdNetSSpSuby> listaRcsorAdNetSSpSuby,
			List<RcsorAdNSerTrRfsSp> listaRcsorAdNSerTrRfsSp, List<RcsorAdNetSSPTby> listaRcsorAdNetSSPTby,
			List<RcsorAdNetSSpTbyRec> listaRcsorAdNetSSpTbyRec, List<RcsorAdNSspUseFacSp> listaRcsorAdNSspUseFacSp,
			List<RcsorAdSeliSpUseNetse> listaRcsorAdSeliSpUseNetse, List<RcsorAdapNetSeSpRel> listaRcsorAdapNetSeSpRel,
			List<RcsopAdapEquipRoleSp> listaRcsopAdapEquipRoleSp,
			List<RcsorAdEqRoRelIsTriBy> listaRcsorAdEqRoRelIsTriBy, List<RcsorANetSeHEquRoSp> listaRcsorANetSeHEquRoSp,
			List<RcsodAdNetSerSpReTy> listaRcsodAdNetSerSpReTy, List<RcsoaAdTaskStepSp> listaRcsoaAdTaskStepSp,
			List<RcsopAdapAccSpec> listaRcsopAdapAccSpec, List<RcsorAdAcSpURSpChVal> listaRcsorAdAcSpURSpChVal,
			List<RcsorAdAccSpULogRSp> listaRcsorAdAccSpULogRSp, List<RcsorAdSerLiUseAcSp> listaRcsorAdSerLiUseAcSp,
			List<RcsorAdAccUseEqRoSp> listaRcsorAdAccUseEqRoSp, List<RcsodAdapFaSpecType> listaRcsodAdapFaSpecType,
			List<RcsopAdapFacSpec> listaRcsopAdapFacSpec, List<RcsorAdFaSHResS> listaRcsorAdFaSHResS,
			List<RcsorAdFaPsChVa> listaRcsorAdFaPsChVa, List<RcsorAdFacSpTrtoRsc> listaRcsorAdFacSpTrtoRsc,
			List<RcsorAdFacSpTrSeSCv> listaRcsorAdFacSpTrSeSCv, List<RcsorAdFacSpTrtoEi> listaRcsorAdFacSpTrtoEi,
			List<RcsorAdEqRoUseFacSp> listaRcsorAdEqRoUseFacSp, List<RcsorAdSlSpUseFaSp> listaRcsorAdSlSpUseFaSp,
			List<RcsorAdapFacSpRel> listaRcsorAdapFacSpRel, List<RcsorAdAccUseFacSpec> listaRcsorAdAccUseFacSpec,
			List<RcsopAdFacValueSpec> listaRcsopAdFacValueSpec, List<RcsorAdFVaSpTrtoGa> listaRcsorAdFVaSpTrtoGa,
			List<RcsorAdapFacVaSusby> listaRcsorAdapFacVaSusby, List<RcsorAdFaVaPsChVa> listaRcsorAdFaVaPsChVa,
			List<RcsorAdFVaSpTrtoSsc> listaRcsorAdFVaSpTrtoSsc, List<RcsorAdFVaSpTrtoRsc> listaRcsorAdFVaSpTrtoRsc,
			List<RcsopAdapTransportSpec> listaRcsopAdapTransportSpec, List<RcsopAdapTrailSp> listaRcsopAdapTrailSp,
			List<RcsorAdNSeReqTrailSp> listaRcsorAdNSeReqTrailSp, List<RcsopAdapNetTrailSp> listaRcsopAdapNetTrailSp,
			List<RcsorAdNetTrUseFaSp> listaRcsorAdNetTrUseFaSp, List<RcsorAdNetTrUseEqRo> listaRcsorAdNetTrUseEqRo,
			List<RcsopAdapParameter> listaRcsopAdapParameter, List<RcsorAdTrailSpULrSp> listaRcsorAdTrailSpULrSp,
			List<RcsorNetLayerHTrail> listaRcsorNetLayerHTrail, List<RcsorNetLayHasSeLine> listaRcsorNetLayHasSeLine,
			List<RcsorNetLayUseAc> listaRcsorNetLayUseAc, List<RcsorAdapNssIsTrigBy> listaRcsorAdapNssIsTrigBy,
			List<RcsorNetLayerHasFac> listaRcsorNetLayerHasFac, List<RcsorNeLayerHNetServ> listaRcsorNeLayerHNetServ,
			List<RcsorNeLayHEqRole> listaRcsorNeLayHEqRole, List<RcsorAdNssHasFacVal> listaRcsorAdNssHasFacVal,
			List<String> fix29) {
		// INSERTAMOS EN PRIMER LUGAR LAS TABLAS BASICAS
		// Nivel 0 Insertamos los type
		insertarTypesNivel0(listaRcsodAdapOperationType, listaRcsodLockVoiceType, listaRcsodAdNetSeTypeSp,
				listaRcsodAdNetSerSpReTy, listaRcsodAdapFaSpecType, listaRcsopAdapParameter);
		// Nivel 1 Insertamos las tablas de nivel 1

		insertarNivel1(listaRcsopAdNetLayerSp, listaRcsopAdapSerLineSp, listaRcsopAdapAccSpec,
				listaRcsopAdapTransportSpec, listaRcsopAdapTrailSp, listaRcsoaAdTaskStepSp);

		insertarNivel2(listaRcsopAdapOperation, listaRcsorAdNetLaySpExeOp, listaRcsorAdapSeLiTrRfsSp,
				listaRcsorAdSeliSpUseNetse, listaRcsopAdapEquipRoleSp, listaRcsorAdAcSpURSpChVal,
				listaRcsorAdAccSpULogRSp, listaRcsorAdSerLiUseAcSp, listaRcsopAdapNetTrailSp, listaRcsopAdapNetSerSp,
				listaRcsopAdapFacSpec, listaRcsorAdTrailSpULrSp, listaRcsorNetLayerHTrail, listaRcsorNetLayHasSeLine,
				listaRcsorNetLayUseAc);

		insertarNivel3(listaRcsoaAdOpTran, listaRcsorAdNetSSpSuby, listaRcsorAdNSerTrRfsSp, listaRcsorAdNetSSPTby,
				listaRcsorAdNetSSpTbyRec, listaRcsorAdNSspUseFacSp, listaRcsorAdSeliSpUseNetse,
				listaRcsorAdEqRoRelIsTriBy, listaRcsorANetSeHEquRoSp, listaRcsorAdAccUseEqRoSp, listaRcsorAdFaSHResS,
				listaRcsorAdFaPsChVa, listaRcsorAdFacSpTrtoRsc, listaRcsorAdFacSpTrSeSCv, listaRcsorAdFacSpTrtoEi,
				listaRcsorAdEqRoUseFacSp, listaRcsorAdSlSpUseFaSp, listaRcsorAdapFacSpRel, listaRcsorAdAccUseFacSpec,
				listaRcsorAdNSeReqTrailSp, listaRcsorAdNetTrUseFaSp, listaRcsorAdNetTrUseEqRo, fix29,
				listaRcsopAdFacValueSpec, listaRcsorAdapNssIsTrigBy, listaRcsorAdapNetSeSpRel, listaRcsorNetLayerHasFac,
				listaRcsorNeLayerHNetServ, listaRcsorNeLayHEqRole);

		insertarNivel4(listaRcsorAdFVaSpTrtoGa, listaRcsorAdapFacVaSusby, listaRcsorAdFaVaPsChVa,
				listaRcsorAdFVaSpTrtoSsc, listaRcsorAdFVaSpTrtoRsc, listaRcsorAdNssHasFacVal);

	}

	/**
	 * @param listaRcsorAdFVaSpTrtoGa
	 * @param listaRcsorAdapFacVaSusby
	 * @param listaRcsorAdFaVaPsChVa
	 * @param listaRcsorAdFVaSpTrtoSsc
	 * @param listaRcsorAdFVaSpTrtoRsc
	 */
	private void insertarNivel4(List<RcsorAdFVaSpTrtoGa> listaRcsorAdFVaSpTrtoGa,
			List<RcsorAdapFacVaSusby> listaRcsorAdapFacVaSusby, List<RcsorAdFaVaPsChVa> listaRcsorAdFaVaPsChVa,
			List<RcsorAdFVaSpTrtoSsc> listaRcsorAdFVaSpTrtoSsc, List<RcsorAdFVaSpTrtoRsc> listaRcsorAdFVaSpTrtoRsc,
			List<RcsorAdNssHasFacVal> listaRcsorAdNssHasFacVal) {

		// 41.- llamamos al metodo SAVE del RcsorAdapFacVaSusbyRepository
		LOGGER.info("Insertamos RELACIONES 41 listaRcsorAdapFacVaSusby");
		rcsorAdapFacVaSusbyRepository.save(listaRcsorAdapFacVaSusby);
		rcsorAdapFacVaSusbyRepository.flush();
		// for (RcsorAdapFacVaSusby rcsorAdapFacVaSusby :
		// listaRcsorAdapFacVaSusby) {
		// rcsorAdapFacVaSusby.getId()
		// .setAfvsIdAdFacValueSpec(rcsorAdapFacVaSusby.getRcsopAdFacValueSpec().getAfvsIdAdFacValueSpec());
		// rcsorAdapFacVaSusby.setRcsopAdFacValueSpec(null);
		// }

		// 42.- llamamos al metodo SAVE del repositorio
		// RcsorAdFaVaPsChVaRepository
		LOGGER.info("Insertamos RELACIONES 42 listaRcsorAdFaVaPsChVa");
		rcsorAdFaVaPsChVaRepository.save(listaRcsorAdFaVaPsChVa);
		rcsorAdFaVaPsChVaRepository.flush();
		// for (RcsorAdFaVaPsChVa RcsorAdFaVaPsChVa : listaRcsorAdFaVaPsChVa) {
		// RcsorAdFaVaPsChVa.getId()
		// .setAfvsIdAdFacValueSpec(RcsorAdFaVaPsChVa.getRcsopAdFacValueSpec().getAfvsIdAdFacValueSpec());
		// RcsorAdFaVaPsChVa.setRcsopAdFacValueSpec(null);
		// }

		// 40.- llamamos al metodo SAVE del repositorio
		// RcsorAdFVaSpTrtoGaRepository
		LOGGER.info("Insertamos RELACIONES 40 listaRcsorAdFVaSpTrtoGa");
		rcsorAdFVaSpTrtoGaRepository.save(listaRcsorAdFVaSpTrtoGa);
		rcsorAdFVaSpTrtoGaRepository.flush();
		// tengo que preparar los objetos de entrada con los ids de salida
		// autogenerados en el paso 39
		// for (RcsorAdFVaSpTrtoGa rcsorAdFVaSpTrtoGa : listaRcsorAdFVaSpTrtoGa)
		// {
		// rcsorAdFVaSpTrtoGa.getId()
		// .setAfvsIdAdFacValueSpec(rcsorAdFVaSpTrtoGa.getRcsopAdFacValueSpec().getAfvsIdAdFacValueSpec());
		// rcsorAdFVaSpTrtoGa.setRcsopAdFacValueSpec(null);
		// }

		// 44.- llamamos al metodo SAVE del RcsorAdFVaSpTrtoRscRepository
		LOGGER.info("Insertamos RELACIONES 44 listaRcsorAdFVaSpTrtoRsc");
		rcsorAdFVaSpTrtoRscRepository.save(listaRcsorAdFVaSpTrtoRsc);
		rcsorAdFVaSpTrtoRscRepository.flush();
		// for (RcsorAdFVaSpTrtoRsc RcsorAdFVaSpTrtoRsc :
		// listaRcsorAdFVaSpTrtoRsc) {
		// RcsorAdFVaSpTrtoRsc.getId()
		// .setAfvsIdAdFacValueSpec(RcsorAdFVaSpTrtoRsc.getRcsopAdFacValueSpec().getAfvsIdAdFacValueSpec());
		// RcsorAdFVaSpTrtoRsc.setRcsopAdFacValueSpec(null);
		// }

		// 43.- llamamos al metodo SAVE del repositorio
		// RcsorAdFVaSpTrtoSscRepository
		LOGGER.info("Insertamos RELACIONES 43 listaRcsorAdFVaSpTrtoSsc");
		rcsorAdFVaSpTrtoSscRepository.save(listaRcsorAdFVaSpTrtoSsc);
		rcsorAdFVaSpTrtoSscRepository.flush();
		// for (RcsorAdFVaSpTrtoSsc RcsorAdFVaSpTrtoSsc :
		// listaRcsorAdFVaSpTrtoSsc) {
		// RcsorAdFVaSpTrtoSsc.getId()
		// .setAfvsIdAdFacValueSpec(RcsorAdFVaSpTrtoSsc.getRcsopAdFacValueSpec().getAfvsIdAdFacValueSpec());
		// RcsorAdFVaSpTrtoSsc.setRcsopAdFacValueSpec(null);
		// }

		// 61.- llamamos al metodo SAVE del repositorio
		// RcsorAdNssHasFacValRepository
		LOGGER.info("Insertamos RELACIONES 61 listaRcsorAdNssHasFacVal");
		rcsorAdNssHasFacValRepository.save(listaRcsorAdNssHasFacVal);
		rcsorAdNssHasFacValRepository.flush();
	}

	/**
	 * @param listaRcsoaAdOpTran
	 * @param listaRcsorAdNetSSpSuby
	 * @param listaRcsorAdNSerTrRfsSp
	 * @param listaRcsorAdNetSSPTby
	 * @param listaRcsorAdNetSSpTbyRec
	 * @param listaRcsorAdNSspUseFacSp
	 * @param listaRcsorAdSeliSpUseNetse
	 * @param listaRcsorAdEqRoRelIsTriBy
	 * @param listaRcsorANetSeHEquRoSp
	 * @param listaRcsorAdAccUseEqRoSp
	 * @param listaRcsorAdFaSHResS
	 * @param listaRcsorAdFaPsChVa
	 * @param listaRcsorAdFacSpTrtoRsc
	 * @param listaRcsorAdFacSpTrSeSCv
	 * @param listaRcsorAdFacSpTrtoEi
	 * @param listaRcsorAdEqRoUseFacSp
	 * @param listaRcsorAdSlSpUseFaSp
	 * @param listaRcsorAdapFacSpRel
	 * @param listaRcsorAdAccUseFacSpec
	 * @param listaRcsorAdNSeReqTrailSp
	 * @param listaRcsorAdNetTrUseFaSp
	 * @param listaRcsorAdNetTrUseEqRo
	 * @param fix29
	 */
	private void insertarNivel3(List<RcsoaAdOpTran> listaRcsoaAdOpTran, List<RcsorAdNetSSpSuby> listaRcsorAdNetSSpSuby,
			List<RcsorAdNSerTrRfsSp> listaRcsorAdNSerTrRfsSp, List<RcsorAdNetSSPTby> listaRcsorAdNetSSPTby,
			List<RcsorAdNetSSpTbyRec> listaRcsorAdNetSSpTbyRec, List<RcsorAdNSspUseFacSp> listaRcsorAdNSspUseFacSp,
			List<RcsorAdSeliSpUseNetse> listaRcsorAdSeliSpUseNetse,
			List<RcsorAdEqRoRelIsTriBy> listaRcsorAdEqRoRelIsTriBy, List<RcsorANetSeHEquRoSp> listaRcsorANetSeHEquRoSp,
			List<RcsorAdAccUseEqRoSp> listaRcsorAdAccUseEqRoSp, List<RcsorAdFaSHResS> listaRcsorAdFaSHResS,
			List<RcsorAdFaPsChVa> listaRcsorAdFaPsChVa, List<RcsorAdFacSpTrtoRsc> listaRcsorAdFacSpTrtoRsc,
			List<RcsorAdFacSpTrSeSCv> listaRcsorAdFacSpTrSeSCv, List<RcsorAdFacSpTrtoEi> listaRcsorAdFacSpTrtoEi,
			List<RcsorAdEqRoUseFacSp> listaRcsorAdEqRoUseFacSp, List<RcsorAdSlSpUseFaSp> listaRcsorAdSlSpUseFaSp,
			List<RcsorAdapFacSpRel> listaRcsorAdapFacSpRel, List<RcsorAdAccUseFacSpec> listaRcsorAdAccUseFacSpec,
			List<RcsorAdNSeReqTrailSp> listaRcsorAdNSeReqTrailSp, List<RcsorAdNetTrUseFaSp> listaRcsorAdNetTrUseFaSp,
			List<RcsorAdNetTrUseEqRo> listaRcsorAdNetTrUseEqRo, List<String> fix29,
			List<RcsopAdFacValueSpec> listaRcsopAdFacValueSpec, List<RcsorAdapNssIsTrigBy> listaRcsorAdapNssIsTrigBy,
			List<RcsorAdapNetSeSpRel> listaRcsorAdapNetSeSpRel, List<RcsorNetLayerHasFac> listaRcsorNetLayerHasFac,
			List<RcsorNeLayerHNetServ> listaRcsorNeLayerHNetServ, List<RcsorNeLayHEqRole> listaRcsorNeLayHEqRole) {
		// 4.- llamamos al metodo SAVE del repositorio
		// RcsoaAdOpTranRepository
		LOGGER.info("Insertamos RELACIONES 4 listaRcsoaAdOpTran");
		rcsoaAdOpTranRepository.save(listaRcsoaAdOpTran);
		rcsoaAdOpTranRepository.flush();
		// 39.- llamamos al metodo SAVE del repositorio
		// RcsopAdFacValueSpecRepository
		LOGGER.info("Insertamos RELACIONES 39 listaRcsopAdFacValueSpec");
		rcsopAdFacValueSpecRepository.save(listaRcsopAdFacValueSpec);
		rcsopAdFacValueSpecRepository.flush();

		// Este save se realiza en el mismo metodo en el que se rellena la lista
		// 38.- llamamos al metodo SAVE del repositorio
		// RcsorAdAccUseFacSpecRepository
		LOGGER.info("Insertamos RELACIONES 38 listaRcsorAdAccUseFacSpec");
		rcsorAdAccUseFacSpecRepository.save(listaRcsorAdAccUseFacSpec);
		rcsorAdAccUseFacSpecRepository.flush();
		// 37.- llamamos al metodo SAVE del repositorio
		// RcsorAdapFacSpRelRepository
		LOGGER.info("Insertamos RELACIONES 37 listaRcsorAdapFacSpRel");
		List<RcsorAdapFacSpRel> listaRcsorAdapFacSpRelAux = new ArrayList<RcsorAdapFacSpRel>();
		for (RcsorAdapFacSpRel adapFacSpecRel : listaRcsorAdapFacSpRel) {
			String afsp1 = adapFacSpecRel.getRcsopAdapFacSpec1().getAfspIdAdapCode();
			String afsp2 = adapFacSpecRel.getRcsopAdapFacSpec1().getAfspIdAdapCode();
			if (fix29.contains(afsp1) && fix29.contains(afsp2)) {
				listaRcsorAdapFacSpRelAux.add(adapFacSpecRel);
			}
		}
		// rcsorAdapFacSpRelRepository.save(listaRcsorAdapFacSpRel);
		rcsorAdapFacSpRelRepository.save(listaRcsorAdapFacSpRelAux);
		rcsorAdapFacSpRelRepository.flush();
		// 35.- llamamos al metodo SAVE del repositorio
		// RcsorAdEqRoUseFacSpRepository
		LOGGER.info("Insertamos RELACIONES 35 listaRcsorAdEqRoUseFacSp");
		rcsorAdEqRoUseFacSpRepository.save(listaRcsorAdEqRoUseFacSp);
		rcsorAdEqRoUseFacSpRepository.flush();
		// 33.- llamamos al metodo SAVE del repositorio
		// RcsorAdFacSpTrSeSCvRepository
		LOGGER.info("Insertamos RELACIONES 33 listaRcsorAdFacSpTrSeSCv");
		rcsorAdFacSpTrSeSCvRepository.save(listaRcsorAdFacSpTrSeSCv);
		rcsorAdFacSpTrSeSCvRepository.flush();
		// 34.- llamamos al metodo SAVE del repositorio
		// RcsorAdFacSpTrtoEiRepository
		LOGGER.info("Insertamos RELACIONES 34 listaRcsorAdFacSpTrtoEi");
		rcsorAdFacSpTrtoEiRepository.save(listaRcsorAdFacSpTrtoEi);
		rcsorAdFacSpTrtoEiRepository.flush();
		// 32.- llamamos al metodo SAVE del repositorio
		// RcsorAdFacSpTrtoRscRepository
		LOGGER.info("Insertamos RELACIONES 32 listaRcsorAdFacSpTrtoRsc");
		rcsorAdFacSpTrtoRscRepository.save(listaRcsorAdFacSpTrtoRsc);
		rcsorAdFacSpTrtoRscRepository.flush();
		// 31.- llamamos al metodo SAVE del repositorio
		// RcsorAdFaPsChVaRepository
		LOGGER.info("Insertamos RELACIONES 31 listaRcsorAdFaPsChVa");
		rcsorAdFaPsChVaRepository.save(listaRcsorAdFaPsChVa);
		rcsorAdFaPsChVaRepository.flush();
		// 30.- llamamos al metodo SAVE del repositorio
		// RcsorAdFaSHResSRepository
		LOGGER.info("Insertamos RELACIONES 30 listaRcsorAdFaSHResS");
		rcsorAdFaSHResSRepository.save(listaRcsorAdFaSHResS);
		rcsorAdFaSHResSRepository.flush();
		// 50.- llamamos al metodo SAVE del repositorio
		// RcsorAdNetTrUseFaSpRepository
		LOGGER.info("Insertamos RELACIONES 50 listaRcsorAdNetTrUseFaSp");
		rcsorAdNetTrUseFaSpRepository.save(listaRcsorAdNetTrUseFaSp);
		rcsorAdNetTrUseFaSpRepository.flush();
		LOGGER.info("Insertamos RELACIONES 15 listaRcsorAdNSspUseFacSp");
		rcsorAdNSspUseFacSpRepository.save(listaRcsorAdNSspUseFacSp);
		rcsorAdNSspUseFacSpRepository.flush();
		// 36.- llamamos al metodo SAVE del repositorio
		// RcsorAdSlSpUseFaSpRepository
		LOGGER.info("Insertamos RELACIONES 36 listaRcsorAdSlSpUseFaSp");
		rcsorAdSlSpUseFaSpRepository.save(listaRcsorAdSlSpUseFaSp);
		rcsorAdSlSpUseFaSpRepository.flush();
		// 27.- llamamos al metodo SAVE del repositorio
		// RcsorAdAccUseEqRoSpRepository
		LOGGER.info("Insertamos RELACIONES 27 listaRcsorAdAccUseEqRoSp");
		rcsorAdAccUseEqRoSpRepository.save(listaRcsorAdAccUseEqRoSp);
		rcsorAdAccUseEqRoSpRepository.flush();
		// 19.- llamamos al metodo SAVE del repositorio
		// RcsorAdEqRoRelIsTriByRepository
		LOGGER.info("Insertamos RELACIONES 19 listaRcsorAdEqRoRelIsTriBy");
		rcsorAdEqRoRelIsTriByRepository.save(listaRcsorAdEqRoRelIsTriBy);
		rcsorAdEqRoRelIsTriByRepository.flush();
		// 11.- llamamos al metodo SAVE del repositorio
		// RcsorAdNetSSpSubyRepository
		LOGGER.info("Insertamos RELACIONES 11 listaRcsorAdNetSSpSuby");
		rcsorAdNetSSpSubyRepository.save(listaRcsorAdNetSSpSuby);
		rcsorAdNetSSpSubyRepository.flush();
		// 13.- llamamos al metodo SAVE del repositorio
		// RcsorAdNetSSpTbyRepository
		LOGGER.info("Insertamos RELACIONES 13 listaRcsorAdNetSSPTby");
		rcsorAdNetSSpTbyRepository.save(listaRcsorAdNetSSPTby);
		rcsorAdNetSSpTbyRepository.flush();
		// 14.- llamamos al metodo SAVE del repositorio
		// RcsorAdNetSSpTbyRecRepository
		LOGGER.info("Insertamos RELACIONES 14 listaRcsorAdNetSSpTbyRec");
		rcsorAdNetSSpTbyRecRepository.save(listaRcsorAdNetSSpTbyRec);
		rcsorAdNetSSpTbyRecRepository.flush();
		// 51.- llamamos al metodo SAVE del repositorio
		// RcsorAdNetTrUseEqRoRepository
		LOGGER.info("Insertamos RELACIONES 51 listaRcsorAdNetTrUseEqRo");
		rcsorAdNetTrUseEqRoRepository.save(listaRcsorAdNetTrUseEqRo);
		rcsorAdNetTrUseEqRoRepository.flush();
		// 48.- llamamos al metodo SAVE del RcsorAdNSeReqTrailSpRepository
		LOGGER.info("Insertamos RELACIONES 48 listaRcsorAdNSeReqTrailSp");
		rcsorAdNSeReqTrailSpRepository.save(listaRcsorAdNSeReqTrailSp);
		rcsorAdNSeReqTrailSpRepository.flush();
		// 12.- llamamos al metodo SAVE del repositorio
		// RcsorAdNSerTrRfsSpRepository
		LOGGER.info("Insertamos RELACIONES 12 listaRcsorAdNSerTrRfsSp");
		rcsorAdNSerTrRfsSpRepository.save(listaRcsorAdNSerTrRfsSp);
		rcsorAdNSerTrRfsSpRepository.flush();
		// 16.- llamamos al metodo SAVE del repositorio
		// RcsorAdSeliSpUseNetseRepository
		LOGGER.info("Insertamos RELACIONES 16 listaRcsorAdSeliSpUseNetse");
		rcsorAdSeliSpUseNetseRepository.save(listaRcsorAdSeliSpUseNetse);
		rcsorAdSeliSpUseNetseRepository.flush();
		// 20.- llamamos al metodo SAVE del repositorio
		// RcsorANetSeHEquRoSpRepository
		LOGGER.info("Insertamos RELACIONES 20 listaRcsorANetSeHEquRoSp");
		rcsorANetSeHEquRoSpRepository.save(listaRcsorANetSeHEquRoSp);
		// 57.- llamamos al metodo SAVE del repositorio
		// RcsorAdapNssIsTrigByRepository
		LOGGER.info("Insertamos RELACIONES 57 listaRcsorAdapNssIsTrigBy");
		rcsorAdapNssIsTrigByRepository.save(listaRcsorAdapNssIsTrigBy);
		rcsorAdapNssIsTrigByRepository.flush();
		// 17.- llamamos al metodo SAVE del repositorio,
		// RcsorAdapNetSeSpRelRepository
		LOGGER.info("Insertamos RELACIONES 17 listaRcsorAdapNetSeSpRel");
		rcsorAdapNetSeSpRelRepository.save(listaRcsorAdapNetSeSpRel);
		rcsorAdapNetSeSpRelRepository.flush();

		// 58.- llamamos al metodo SAVE del repositorio
		LOGGER.info("Insertamos RELACIONES 58 listaRcsorNetLayerHasFac");
		List<RcsorNetLayerHasFac> listaRcsorNetLayerHasFacAux = new ArrayList<RcsorNetLayerHasFac>();
		for (RcsorNetLayerHasFac rcsorNetLayerHasFac : listaRcsorNetLayerHasFac) {
			if (!rcsorNetLayerHasFac.getRcsopAdNetLayerSp().getAnlsIdAdapCode().equals(vacio)) {
				listaRcsorNetLayerHasFacAux.add(rcsorNetLayerHasFac);
			}
		}
		rcsorNetLayerHasFacRepository.save(listaRcsorNetLayerHasFacAux);
		rcsorNetLayerHasFacRepository.flush();
		// // 59.- llamamos al metodo SAVE del repositorio
		LOGGER.info("Insertamos RELACIONES 59 listaRcsorNeLayerHNetServ");
		rcsorNeLayerHNetServRepository.save(listaRcsorNeLayerHNetServ);
		rcsorNeLayerHNetServRepository.flush();
		// // 60.- llamamos al metodo SAVE del repositorio
		LOGGER.info("Insertamos RELACIONES 60 listaRcsorNeLayHEqRole");
		rcsorNeLayHEqRoleRepository.save(listaRcsorNeLayHEqRole);
		rcsorNeLayHEqRoleRepository.flush();
	}

	/**
	 * @param listaRcsopAdapOperation
	 * @param listaRcsorAdNetLaySpExeOp
	 * @param listaRcsorAdapSeLiTrRfsSp
	 * @param listaRcsorAdSeliSpUseNetse
	 * @param listaRcsorAdapNetSeSpRel
	 * @param listaRcsopAdapEquipRoleSp
	 * @param listaRcsorAdAcSpURSpChVal
	 * @param listaRcsorAdAccSpULogRSp
	 * @param listaRcsorAdSerLiUseAcSp
	 * @param listaRcsopAdapNetTrailSp
	 */
	private void insertarNivel2(List<RcsopAdapOperation> listaRcsopAdapOperation,
			List<RcsorAdNetLaySpExeOp> listaRcsorAdNetLaySpExeOp, List<RcsorAdapSeLiTrRfsSp> listaRcsorAdapSeLiTrRfsSp,
			List<RcsorAdSeliSpUseNetse> listaRcsorAdSeliSpUseNetse,
			List<RcsopAdapEquipRoleSp> listaRcsopAdapEquipRoleSp, List<RcsorAdAcSpURSpChVal> listaRcsorAdAcSpURSpChVal,
			List<RcsorAdAccSpULogRSp> listaRcsorAdAccSpULogRSp, List<RcsorAdSerLiUseAcSp> listaRcsorAdSerLiUseAcSp,
			List<RcsopAdapNetTrailSp> listaRcsopAdapNetTrailSp, List<RcsopAdapNetSerSp> listaRcsopAdapNetSerSp,
			List<RcsopAdapFacSpec> listaRcsopAdapFacSpec, List<RcsorAdTrailSpULrSp> listaRcsorAdTrailSpULrSp,
			List<RcsorNetLayerHTrail> listaRcsorNetLayerHTrail, List<RcsorNetLayHasSeLine> listaRcsorNetLayHasSeLine,
			List<RcsorNetLayUseAc> listaRcsorNetLayUseAc) {
		// 3.- llamamos al metodo SAVE del repositorio
		// RcsopAdapOperationRepository
		LOGGER.info("Insertamos RELACIONES 3 listaRcsopAdapTrailSp");
		for (int i = 0; i < listaRcsopAdapOperation.size(); i++) {
			listaRcsopAdapOperation.get(i).setRcsodAdapOperationType(rcsodAdapOperationTypeRepository
					.findOne(listaRcsopAdapOperation.get(i).getRcsodAdapOperationType().getAoptIdAdapOperationType()));
		}
		rcsopAdapOperationRepository.save(listaRcsopAdapOperation);
		rcsopAdapOperationRepository.flush();
		// 18.- llamamos al metodo SAVE del repositorio
		// RcsopAdapEquipRoleSpRepository
		LOGGER.info("Insertamos RELACIONES 18 listaRcsopAdapEquipRoleSp");
		rcsopAdapEquipRoleSpRepository.save(listaRcsopAdapEquipRoleSp);
		rcsopAdapEquipRoleSpRepository.flush();
		// 6.- llamamos al metodo SAVE del repositorio
		// RcsorAdNetLaySpExeOpRepository
		LOGGER.info("Insertamos RELACIONES 6 listaRcsorAdNetLaySpExeOp");
		rcsorAdNetLaySpExeOpRepository.save(listaRcsorAdNetLaySpExeOp);
		rcsorAdNetLaySpExeOpRepository.flush();
		// 15.- llamamos al metodo SAVE del repositorio
		// RcsorAdNSspUseFacSpRepository (el 15 por ahora lo vamos a dejar
		// sin hacer y seguiremos con el 16)
		// 49.- llamamos al metodo SAVE del repositorio
		// RcsopAdapNetTrailSpRepository
		LOGGER.info("Insertamos RELACIONES 49 listaRcsopAdapNetTrailSp");
		rcsopAdapNetTrailSpRepository.save(listaRcsopAdapNetTrailSp);
		rcsopAdapNetTrailSpRepository.flush();
		// 25.- llamamos al metodo SAVE del repositorio
		// RcsorAdAccSpULogRSpRepository
		LOGGER.info("Insertamos RELACIONES 25 listaRcsorAdAccSpULogRSp");
		rcsorAdAccSpULogRSpRepository.save(listaRcsorAdAccSpULogRSp);
		rcsorAdAccSpULogRSpRepository.flush();
		// 24.- llamamos al metodo SAVE del repositorio
		// RcsorAdAcSpURSpChValRepository
		LOGGER.info("Insertamos RELACIONES 24 listaRcsorAdAcSpURSpChVal");
		rcsorAdAcSpURSpChValRepository.save(listaRcsorAdAcSpURSpChVal);
		rcsorAdAcSpURSpChValRepository.flush();
		// 8.- llamamos al metodo SAVE del repositorio
		// RcsorAdapSeLiTrRfsSpRepository
		LOGGER.info("Insertamos RELACIONES 8 listaRcsorAdapSeLiTrRfsSp");
		rcsorAdapSeLiTrRfsSpRepository.save(listaRcsorAdapSeLiTrRfsSp);
		rcsorAdapSeLiTrRfsSpRepository.flush();
		// 26.- llamamos al metodo SAVE del respositorio
		// RcsorAdSerLiUseAcSpRepository
		LOGGER.info("Insertamos RELACIONES 26 listaRcsorAdSerLiUseAcSp");
		rcsorAdSerLiUseAcSpRepository.save(listaRcsorAdSerLiUseAcSp);
		rcsorAdSerLiUseAcSpRepository.flush();
		// 9.- llamamos al metodo SAVE del repositorio
		// RcsopAdapNetSerSpRepository
		LOGGER.info("Insertamos RELACIONES 9 RcsopAdapNetSerSpRepository");
		rcsopAdapNetSerSpRepository.save(listaRcsopAdapNetSerSp);
		rcsopAdapNetSerSpRepository.flush();
		// 29.- llamamos al estado SAVE del repositorio
		// RcsopAdapFacSpecRepository
		LOGGER.info("Insertamos RELACIONES 29 listaRcsopAdapFacSpec");
		rcsopAdapFacSpecRepository.save(listaRcsopAdapFacSpec);
		// 53.- llamamos al estado SAVE del repositorio
		// RcsorAdTrailSpULrSpRepository
		LOGGER.info("Insertamos RELACIONES 53 listaRcsorAdTrailSpULrSp");
		rcsorAdTrailSpULrSpRepository.save(listaRcsorAdTrailSpULrSp);
		rcsorAdTrailSpULrSpRepository.flush();
		// // 54.- llamamos al estado SAVE del repositorio
		// // RcsorNetLayerHTrailRepository
		LOGGER.info("Insertamos RELACIONES 54 listaRcsorNetLayerHTrail");
		rcsorNetLayerHTrailRepository.save(listaRcsorNetLayerHTrail);
		rcsorNetLayerHTrailRepository.flush();
		// // 55.- llamamos al estado SAVE del repositorio
		// // RcsorNetLayHasSeLineRepository
		LOGGER.info("Insertamos RELACIONES 55 listaRcsorNetLayHasSeLine");
		rcsorNetLayHasSeLineRepository.save(listaRcsorNetLayHasSeLine);
		rcsorNetLayHasSeLineRepository.flush();
		// // 56.- llamamos al estado SAVE del repositorio
		// // RcsorNetLayUseAcRepository
		LOGGER.info("Insertamos RELACIONES 56 listaRcsorNetLayUseAc");
		rcsorNetLayUseAcRepository.save(listaRcsorNetLayUseAc);
		rcsorNetLayUseAcRepository.flush();
	}

	/**
	 * @param listaRcsopAdNetLayerSp
	 * @param listaRcsopAdapSerLineSp
	 * @param listaRcsopAdapAccSpec
	 * @param listaRcsopAdapTransportSpec
	 * @param listaRcsopAdapTrailSp
	 */
	private void insertarNivel1(List<RcsopAdNetLayerSp> listaRcsopAdNetLayerSp,
			List<RcsopAdapSerLineSp> listaRcsopAdapSerLineSp, List<RcsopAdapAccSpec> listaRcsopAdapAccSpec,
			List<RcsopAdapTransportSpec> listaRcsopAdapTransportSpec, List<RcsopAdapTrailSp> listaRcsopAdapTrailSp,
			List<RcsoaAdTaskStepSp> listaRcsoaAdTaskStepSp) {
		LOGGER.info("Insertamos BASICAS 1 listaRcsopAdNetLayerSp");
		List<RcsopAdNetLayerSp> listaRcsopAdNetLayerSpAux = new ArrayList<RcsopAdNetLayerSp>();
		for (RcsopAdNetLayerSp rcsopAdNetLayerSp : listaRcsopAdNetLayerSp) {
			if (!rcsopAdNetLayerSp.getAnlsIdAdapCode().equals(vacio)) {
				listaRcsopAdNetLayerSpAux.add(rcsopAdNetLayerSp);
			}
		}
		rcsopAdNetLayerSpRepository.save(listaRcsopAdNetLayerSpAux);
		rcsopAdNetLayerSpRepository.flush();
		// 45.- llamamos al metodo SAVE del RcsopAdapTransportSpecRepository
		LOGGER.info("Insertamos BASICAS 45 listaRcsopAdapTransportSpec");
		rcsopAdapTransportSpecRepository.save(listaRcsopAdapTransportSpec);
		rcsopAdapTransportSpecRepository.flush();
		// 46.- llamamos al metodo SAVE del RcsopAdapTrailSpRepository
		LOGGER.info("Insertamos BASICAS 46 listaRcsopAdapTrailSp");
		rcsopAdapTrailSpRepository.save(listaRcsopAdapTrailSp);
		rcsopAdapTrailSpRepository.flush();
		// 23.- llamamos al metodo SAVE del repositorio
		// RcsopAdapAccSpecRepository
		LOGGER.info("Insertamos BASICAS 23 listaRcsopAdapAccSpec");
		rcsopAdapAccSpecRepository.save(listaRcsopAdapAccSpec);
		rcsopAdapAccSpecRepository.flush();
		// 7.- llamamos al metodo SAVE del repositorio
		// RcsopAdapSerLineSpRepository
		LOGGER.info("Insertamos BASICAS 7 listaRcsopAdapSerLineSp");
		rcsopAdapSerLineSpRepository.save(listaRcsopAdapSerLineSp);
		rcsopAdapSerLineSpRepository.flush();
		// 22.- llamamos al metodo SAVE del repositorio
		// RcsoaAdTaskStepSpRepository
		LOGGER.info("Insertamos BASICAS 22 listaRcsoaAdTaskStepSp");
		rcsoaAdTaskStepSpRepository.save(listaRcsoaAdTaskStepSp);
		rcsoaAdTaskStepSpRepository.flush();
	}

	/**
	 * @param listaRcsodAdapOperationType
	 * @param listaRcsodLockVoiceType
	 * @param listaRcsodAdNetSeTypeSp
	 */
	private void insertarTypesNivel0(List<RcsodAdapOperationType> listaRcsodAdapOperationType,
			List<RcsodLockVoiceType> listaRcsodLockVoiceType, List<RcsodAdNetSeTypeSp> listaRcsodAdNetSeTypeSp,
			List<RcsodAdNetSerSpReTy> listaRcsodAdNetSerSpReTy, List<RcsodAdapFaSpecType> listaRcsodAdapFaSpecType,
			List<RcsopAdapParameter> listaRcsopAdapParameter) {
		// UMD_GSDRED4 .RCSOD_ADAP_XML_TYPE
		// UMD_GSDRED4 .RCSOD_ADAP_FA_SPEC_TYPE
		// UMD_GSDRED4 .RCSOD_LOCK_VOICE_TYPE
		// UMD_GSDRED4 .RCSOD_ADAP_OPERATION_TYPE
		// UMD_GSDRED4 .RCSOD_AD_NET_SE_TYPE_SP
		// 2.- llamamos al metodo SAVE del repositorio
		// RcsodAdapOperationTypeRepository
		// //52.- llamamos al metodo SAVE del repositorio
		// // RcsopAdapParameterRepository
		LOGGER.info("Insertamos BASICAS 52 listaRcsopAdapParameter");
		rcsopAdapParameterRepository.save(listaRcsopAdapParameter);
		rcsopAdapParameterRepository.flush();
		LOGGER.info("Insertamos BASICAS 2 listaRcsodAdapOperationType");
		rcsodAdapOperationTypeRepository.save(listaRcsodAdapOperationType);
		rcsodAdapOperationTypeRepository.flush();
		// 5.- llamamos al metodo SAVE del repositorio
		// RcsodLockVoiceTypeRepository
		LOGGER.info("Insertamos BASICAS 5 listaRcsodLockVoiceType");
		rcsodLockVoiceTypeRepository.save(listaRcsodLockVoiceType);
		rcsodLockVoiceTypeRepository.flush();
		// 10.- llamamos al metodo SAVE del repositorio
		// RcsodAdNetSeTypeSPRepository
		LOGGER.info("Insertamos BASICAS 10 listaRcsodAdNetSeTypeSp");
		rcsodAdNetSeTypeSpRepository.save(listaRcsodAdNetSeTypeSp);
		rcsodAdNetSeTypeSpRepository.flush();
		// 21.- llamamos al metodo SAVE del repositorio
		// RcsodAdNetSerSpReTyRepository
		LOGGER.info("Insertamos BASICAS 21 listaRcsodAdNetSerSpReTy");
		rcsodAdNetSerSpReTyRepository.save(listaRcsodAdNetSerSpReTy);
		rcsodAdNetSerSpReTyRepository.flush();
		// 28.- llamamos al metodo SAVE del repositorio
		// RcsodAdapFaSpecTypeRepository
		LOGGER.info("Insertamos BASICAS 28 listaRcsodAdapFaSpecType");
		rcsodAdapFaSpecTypeRepository.save(listaRcsodAdapFaSpecType);
		rcsodAdapFaSpecTypeRepository.flush();
	}

	/**
	 * Servicio de Acceso a Entidad que actualiza el valor de un parámetro de
	 * adaptación
	 * 
	 * @param updateadapparametersbyadapcode_in
	 * @param TE_Cabecera
	 *            te_Cabecera
	 * @param TE_Metadatos
	 *            te_Metadatos
	 * @return UpdateAdapParametersByAdapCode_OUT
	 * @throws TE_Excepcion
	 */
	public UpdateAdapParametersByAdapCode_OUT updateAdapParametersByAdapCode(
			UpdateAdapParametersByAdapCode_IN updateadapparametersbyadapcode_in, TE_Cabecera te_Cabecera,
			TE_Metadatos te_Metadatos) throws TE_Excepcion {
		UpdateAdapParametersByAdapCode_OUT out = new UpdateAdapParametersByAdapCode_OUT();
		try {
			for (com.telefonica.gdre.srv.nuc.adapmodel.msg.updateadapparametersbyadapcode.AdapParameters_DTO_IN elto : updateadapparametersbyadapcode_in
					.getAdapParameters()) {
				RcsopAdapParameter result = new RcsopAdapParameter();
				result = rcsopAdapParameterRepository.getOne(elto.getAdapCode());
				result.setAdpaNaValue(elto.getValue());
				rcsopAdapParameterRepository.save(result);
			}
		} catch (PersistenceException e) {
			throw new TE_Excepcion(ERROR_000101, DESCRIPTION_000101);
		}

		return out;
	}

	private com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT[] creaAdapFacilitySpecRel(
			MapasCache mapasCache, String rcsopAdapFacSpecRel) {

		// Tamaño array
		int tamanyo = 0;

		// Lista de RcsorAdapFacSpRel
		List<RcsorAdapFacSpRel> listaRcsorAdapFacSpRel = null;

		// Objeto de salida con el tamaño indicado
		com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT[] arrayAdapFacilitySpecRelOut = null;

		// Comprobamos que no este vacio el objeto y la lista
		if (mapasCache.getMapaAdapFacSpRel() != null
				&& mapasCache.getMapaAdapFacSpRel().get(rcsopAdapFacSpecRel) != null) {
			tamanyo = mapasCache.getMapaAdapFacSpRel().get(rcsopAdapFacSpecRel).size();
			listaRcsorAdapFacSpRel = mapasCache.getMapaAdapFacSpRel().get(rcsopAdapFacSpecRel);

			arrayAdapFacilitySpecRelOut = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT[tamanyo];

			/*
			 * Empezamos a recorrer toda la lista para mapear el objeto de
			 * salida
			 */
			for (int i = 0; i < tamanyo; i++) {

				// Objeto de mapeo para asignar al objeto de salida
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT adapFacilitySpecRelOut = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT();

				// mapeamos propiedades básicas
				adapFacilitySpecRelOut.setMaxCardinality(listaRcsorAdapFacSpRel.get(i).getAfsrQuMaxCardinality());
				adapFacilitySpecRelOut.setMinCardinality(listaRcsorAdapFacSpRel.get(i).getAfsrQuMinCardinality());
				adapFacilitySpecRelOut.setOrder(listaRcsorAdapFacSpRel.get(i).getAfsrNuOrder());

				// Mapeamos objeto AdapFacilitySpec_DTO_OUT
				com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpec_DTO_OUT adapFacilitySpecOut = null;

				// Si el objeto no existe lo busca
				if (null == mapasCache.getMapaAdapFacilitySpecDTOOUT()
						.get(listaRcsorAdapFacSpRel.get(i).getRcsopAdapFacSpec2().getAfspIdAdapCode())) {
					adapFacilitySpecOut = creaAdapFacilitySpec(mapasCache,
							listaRcsorAdapFacSpRel.get(i).getRcsopAdapFacSpec2().getAfspIdAdapCode());
				} else {
					adapFacilitySpecOut = mapasCache.getMapaAdapFacilitySpecDTOOUT()
							.get(listaRcsorAdapFacSpRel.get(i).getRcsopAdapFacSpec2().getAfspIdAdapCode());
				}

				adapFacilitySpecRelOut.setAdapFacilitySpec(adapFacilitySpecOut);

				arrayAdapFacilitySpecRelOut[i] = adapFacilitySpecRelOut;

			}

		} else {
			arrayAdapFacilitySpecRelOut = new com.telefonica.gdre.srv.nuc.adapmodel.msg.getwholeadaptationmodelbyadapnetworklayerspec.AdapFacilitySpecRel_DTO_OUT[tamanyo];
		}

		return arrayAdapFacilitySpecRelOut;

	}

	// ResourceSpecCharacteristicIds
	private Long[] creaResourceSpecCharacteristicIds(MapasCache mapasCache, String rcsorAdFacSpTrtoRsc) {

		// Creamos objeto salida
		Long[] idSalida = new Long[0];

		// Comprobamos que no este vacio el objeto y la lista
		if (mapasCache.getMapaAdFacSpTrtoRsc() != null
				&& mapasCache.getMapaAdFacSpTrtoRsc().get(rcsorAdFacSpTrtoRsc) != null) {

			// Creamos lista del objeto para obtener sus id
			List<RcsorAdFacSpTrtoRsc> lista = mapasCache.getMapaAdFacSpTrtoRsc().get(rcsorAdFacSpTrtoRsc);

			// Creamos array con tamaño fijo de Long
			idSalida = new Long[mapasCache.getMapaAdFacSpTrtoRsc().get(rcsorAdFacSpTrtoRsc).size()];

			/*
			 * Recorremos todos los objetos RcsorAdFacSpTrtoRsc para obtener su
			 * id Long
			 */
			for (int i = 0; i < mapasCache.getMapaAdFacSpTrtoRsc().get(rcsorAdFacSpTrtoRsc).size(); i++) {

				idSalida[i] = lista.get(i).getId().getRschIdResourceSpChar();
			}

		}

		return idSalida;
	}
}