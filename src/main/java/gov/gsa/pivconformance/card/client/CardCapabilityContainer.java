package gov.gsa.pivconformance.card.client;

import gov.gsa.pivconformance.tlv.*;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class CardCapabilityContainer extends PIVDataObject {
    // slf4j will thunk this through to an appropriately configured logging library
    private static final Logger s_logger = LoggerFactory.getLogger(CardCapabilityContainer.class);

    private byte[] m_cardIdentifier;
    private byte[] m_capabilityContainerVersionNumber;
    private byte[] m_capabilityGrammarVersionNumber;
    private List<byte[]> m_appCardURL;
    private byte[] m_pkcs15;
    private byte[] m_registeredDataModelNumber;
    private byte[] m_accessControlRuleTable;
    private boolean m_cardAPDUs;
    private boolean m_redirectionTag;
    private boolean m_capabilityTuples;
    private boolean m_statusTuples;
    private boolean m_nextCCC;

    private List<byte[]> m_extendedApplicationCardURL;
    private byte[] m_securityObjectBuffer;


    private boolean m_errorDetectionCode;

    private boolean m_parsed;

    public CardCapabilityContainer() {

        m_cardIdentifier = null;
        m_capabilityContainerVersionNumber = null;
        m_capabilityGrammarVersionNumber = null;
        m_appCardURL = null;
        m_pkcs15 = null;
        m_registeredDataModelNumber = null;
        m_accessControlRuleTable = null;
        m_cardAPDUs = false;
        m_redirectionTag = false;
        m_capabilityTuples = false;
        m_statusTuples = false;
        m_nextCCC = false;
        m_extendedApplicationCardURL = null;
        m_securityObjectBuffer = null;
        m_errorDetectionCode = false;
        m_parsed = false;
    }

    public byte[] getCardIdentifier() {

        return m_cardIdentifier;
    }

    public byte[] getCapabilityContainerVersionNumber() {

        return m_capabilityContainerVersionNumber;
    }

    public byte[] getCapabilityGrammarVersionNumber() {

        return m_capabilityGrammarVersionNumber;
    }

    public List<byte[]> getAppCardURL() {

        return m_appCardURL;
    }

    public byte[] getPkcs15() {

        return m_pkcs15;
    }

    public byte[] getRegisteredDataModelNumber() {

        return m_registeredDataModelNumber;
    }

    public byte[] getAccessControlRuleTable() {

        return m_accessControlRuleTable;
    }

    public boolean getCardAPDUs() {

        return m_cardAPDUs;
    }

    public boolean getRedirectionTag() {

        return m_redirectionTag;
    }

    public boolean getCapabilityTuples() {

        return m_capabilityTuples;
    }

    public boolean getStatusTuples() {

        return m_statusTuples;
    }

    public boolean getNextCCC() {

        return m_nextCCC;
    }

    public List<byte[]> getExtendedApplicationCardURL() {

        return m_extendedApplicationCardURL;
    }

    public byte[] getSecurityObjectBuffer() {

        return m_securityObjectBuffer;
    }

    public boolean getErrorDetectionCode() {

        return m_errorDetectionCode;
    }


    public boolean decode() {

        try{
            byte [] raw = super.getBytes();

            if(raw == null){
                s_logger.error("No buffer to decode for {}.", APDUConstants.oidNameMAP.get(super.getOID()));
                return false;
            }

            BerTlvParser tp = new BerTlvParser(new CCTTlvLogger(CardCapabilityContainer.class));
            BerTlvs outer = tp.parse(raw);

            if(outer == null){
                s_logger.error("Error parsing X.509 Certificate, unable to parse TLV value.");
                return false;
            }

            List<BerTlv> values = outer.getList();
            for(BerTlv tlv : values) {
                if(tlv.isPrimitive()) {
                    s_logger.info("Tag {}: {}", Hex.encodeHexString(tlv.getTag().bytes), Hex.encodeHexString(tlv.getBytesValue()));

                    BerTlvs outer2 = tp.parse(tlv.getBytesValue());

                    if(outer2 == null){
                        s_logger.error("Error parsing X.509 Certificate, unable to parse TLV value.");
                        return false;
                    }

                    List<BerTlv> values2 = outer2.getList();
                    for(BerTlv tlv2 : values2) {
                        if(tlv2.isPrimitive()) {
                            s_logger.info("Tag {}: {}", Hex.encodeHexString(tlv2.getTag().bytes), Hex.encodeHexString(tlv2.getBytesValue()));
                        } else {
                            if(Arrays.equals(tlv2.getTag().bytes,TagConstants.CARD_IDENTIFIER_TAG)) {
                                if (tlv2.hasRawValue()) {
                                    m_cardIdentifier = tlv2.getBytesValue();
                                }
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.CAPABILITY_CONTAINER_VERSION_NUMBER_TAG)) {
                                if (tlv2.hasRawValue()) {
                                    m_capabilityContainerVersionNumber = tlv2.getBytesValue();
                                }
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.CAPABILITY_GRAMMAR_VERSION_NUMBER_TAG)) {
                                if (tlv2.hasRawValue()) {
                                    m_capabilityGrammarVersionNumber = tlv2.getBytesValue();
                                }
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.APPLICATIONS_CARDURL_TAG)) {
                                if (tlv2.hasRawValue()) {

                                    if(m_appCardURL == null)
                                        m_appCardURL = new ArrayList<>();
                                    m_appCardURL.add(tlv2.getBytesValue());
                                }
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.PKCS15_TAG)) {
                                if (tlv2.hasRawValue()) {
                                    m_pkcs15 = tlv2.getBytesValue();
                                }
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.REGISTERED_DATA_MODEL_NUMBER_TAG)) {
                                if (tlv2.hasRawValue()) {
                                    m_registeredDataModelNumber = tlv2.getBytesValue();
                                }
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.ACCESS_CONTROL_RULE_TABLE_TAG)) {
                                if (tlv2.hasRawValue()) {
                                    m_accessControlRuleTable = tlv2.getBytesValue();
                                }
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.CARD_APDUS_TAG)) {
                                    m_cardAPDUs = true;
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.REDIRECTION_TAG_TAG)) {
                                    m_redirectionTag = true;
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.CAPABILITY_TUPLES_TAG)) {
                                    m_capabilityTuples = true;
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.STATUS_TUPLES_TAG)) {
                                    m_statusTuples = true;
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.NEXT_CCC_TAG)) {
                                    m_nextCCC = true;
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.EXTENDED_APPLICATION_CARDURL_TAG)) {
                                if(m_extendedApplicationCardURL == null)
                                    m_extendedApplicationCardURL = new ArrayList<>();
                                m_extendedApplicationCardURL.add(tlv2.getBytesValue());
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.SECURITY_OBJECT_BUFFER_TAG)) {
                                    m_securityObjectBuffer = tlv2.getBytesValue();
                            }
                            if(Arrays.equals(tlv2.getTag().bytes, TagConstants.ERROR_DETECTION_CODE_TAG)) {
                                    m_errorDetectionCode = true;
                            }
                        }
                    }
                } else {
                    s_logger.info("Object: {}", Hex.encodeHexString(tlv.getTag().bytes));
                }
            }
        }catch (Exception ex) {

            s_logger.error("Error parsing X.509 Certificate: {}", ex.getMessage());
        }
        m_parsed = true;

        return true;
    }

}
