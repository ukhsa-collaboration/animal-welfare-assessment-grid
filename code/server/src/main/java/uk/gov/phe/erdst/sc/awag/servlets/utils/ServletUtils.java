package uk.gov.phe.erdst.sc.awag.servlets.utils;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.datamodel.client.PagingGetRequestParams;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayloadConstants;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.ResponseWrapper;

public final class ServletUtils
{
    private static final int BUFFER_SIZE = 512;
    private static final Logger LOGGER = LogManager.getLogger(ServletUtils.class.getName());

    private ServletUtils()
    {
    }

    public static Long getParseResourceId(String sId) throws AWInvalidResourceIdException
    {
        Long id = null;
        try
        {
            id = Long.parseLong(sId);
        }
        catch (NumberFormatException ex)
        {
            throw new AWInvalidResourceIdException(ValidationConstants.ERR_RESOURCE_ID_NOT_NUMBER);
        }
        return id;
    }

    /**
     * Parses the string id to a long. Returns null if the string id is not a number.
     */
    public static Long getIdParameterFromString(String sId)
    {
        try
        {
            return Long.parseLong(sId);
        }
        catch (NumberFormatException ex)
        {
            return null;
        }
    }

    /**
     * Parses the string to a Boolean. Returns null if the string is null.
     */
    public static Boolean getBooleanParameterFromString(String value)
    {
        return value != null ? Boolean.parseBoolean(value) : null;
    }

    // FIXME: what is the difference between noResponse() and noResponsePayload() ?
    public static String getNoResponse()
    {
        return ServletConstants.RES_NO_RESPONSE;
    }

    public static Object getNoResponsePayload()
    {
        return new Object();
    }

    public static void setResponseOk(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public static void setResponseCreated(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    public static void setResponseDeleteOk(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public static void setResponseServerError(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public static void setResponseClientError(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    public static void setResponseResourceNotFound(HttpServletResponse response)
    {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private static String getResourceIdPart(HttpServletRequest request) throws CharConversionException
    {
        String uri = null;
        try
        {
            uri = URLDecoder.decode(request.getRequestURI(), Constants.AW_CHARSET.toString());
        }
        catch (UnsupportedEncodingException ex)
        {
            LOGGER.error(ex.getMessage());
        }
        return uri.substring(uri.lastIndexOf("/"), uri.length()).replace("/", "");
    }

    public static String getStringResourceId(HttpServletRequest request) throws CharConversionException
    {
        return getResourceIdPart(request);
    }

    public static Long getNumberResourceId(HttpServletRequest request) throws CharConversionException
    {
        try
        {
            return Long.parseLong(getResourceIdPart(request));
        }
        catch (NumberFormatException ex)
        {
            return null;
        }
    }

    public static void setResponseBody(HttpServletResponse response, String responseText) throws IOException
    {
        Writer writer = null;
        try
        {
            writer = response.getWriter();
            writer.write(responseText);
        }
        catch (IOException ex)
        {
            throw new IOException(ex.getMessage());
        }
        finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }
    }

    public static String getRequestBody(HttpServletRequest request) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder("");
        Reader reader = null;
        try
        {
            if (request != null)
            {
                reader = request.getReader();
                char[] charBuffer = new char[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = reader.read(charBuffer)) > 0)
                {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        }
        catch (IOException ex)
        {
            throw ex;
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ex)
                {
                    throw ex;
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void printResponse(HttpServletResponse response, String callback, String data)
    {
        PrintWriter mOut = null;
        try
        {
            mOut = response.getWriter();
            mOut.println(ResponseWrapper.wrap(callback, data));
        }
        catch (IOException e)
        {
            // TODO: add loging
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally
        {
            if (mOut != null)
            {
                mOut.close();
            }
        }
    }

    public static String getCallbackParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_CALLBACK,
            ValidationConstants.ERR_CALLBACK_PARAM);
    }

    public static String getSelectActionParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_ACTION, ValidationConstants.ERR_ACTION_PARAM);
    }

    public static String getSelectLikeParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_LIKE, ValidationConstants.ERR_LIKE_PARAM);
    }

    public static void setJsonContentType(HttpServletResponse response)
    {
        response.setContentType("text/javascript");
    }

    public static String getSelectEntityIdParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_ID, ValidationConstants.ERR_ID_PARAM);
    }

    public static String getSelectLikeSexParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return request.getParameter(ServletConstants.REQ_PARAM_SEX);
    }

    public static String getSelectLikeAllParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return request.getParameter(ServletConstants.REQ_PARAM_ALL);
    }

    public static String getAnimalIdParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_ANIMAL_ID,
            ValidationConstants.ERR_ANIMAL_ID_PARAM);
    }

    public static String getHousingParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_HOUSING, ValidationConstants.ERR_HOUSING_PARAM);
    }

    public static String getStudyGroupParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_STUDY_GROUP,
            ValidationConstants.ERR_STUDY_GROUP_PARAM);
    }

    /**
     * Gets a parameter from the given request. The returned value may be null.
     */
    public static String getParameter(HttpServletRequest request, String paramName)
    {
        return request.getParameter(paramName);
    }

    public static String getNonNullParameter(HttpServletRequest request, String paramName, String errorMsg)
        throws AWInvalidParameterException
    {
        String param = request.getParameter(paramName);
        if (param == null)
        {
            throw new AWInvalidParameterException(errorMsg);
        }

        return param;
    }

    public static String getTemplateIdParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return getNonNullParameter(request, ServletConstants.REQ_PARAM_TEMPLATE_ID,
            ValidationConstants.ERR_TEMPLATE_ID_PARAM);
    }

    public static String getDateFromParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return request.getParameter(ServletConstants.REQ_PARAM_DATE_FROM);
    }

    public static String getDateToParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return request.getParameter(ServletConstants.REQ_PARAM_DATE_TO);
    }

    public static String getDateParameter(HttpServletRequest request) throws AWInvalidParameterException
    {
        return request.getParameter(ServletConstants.REQ_PARAM_DATE);
    }

    public static boolean getIncludeMetadataParameter(HttpServletRequest request)
    {
        String include = request.getParameter(ServletConstants.REQ_PARAM_INCLUDE);

        return ("metadata".equals(include));
    }

    public static Integer getOffsetParameter(HttpServletRequest request)
    {
        Integer offset;
        try
        {
            offset = Integer.valueOf(request.getParameter(ResponsePayloadConstants.META_OFFSET));
        }
        catch (NumberFormatException ex)
        {
            offset = null;
        }
        return offset;
    }

    public static Integer getLimitParameter(HttpServletRequest request)
    {
        Integer limit;
        try
        {
            limit = Integer.valueOf(request.getParameter(ResponsePayloadConstants.META_LIMIT));
        }
        catch (NumberFormatException ex)
        {
            limit = null;
        }
        return limit;
    }

    public static boolean isDeleteTemplate(String[] parts)
    {
        // TODO: check parts match
        return false;
    }

    public static boolean isDeleteTemplateParameter(String[] parts)
    {
        if (parts.length == 3)
        {
            String parameterPathPart = parts[1];

            if (!parameterPathPart.equals("parameter"))
            {
                return false;
            }

            try
            {
                Long.parseLong(parts[0]);
                Long.parseLong(parts[2]);
            }
            catch (NumberFormatException ex)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public static PagingGetRequestParams getPagingRequestParams(Integer offset, Integer limit)
    {
        PagingGetRequestParams params = new PagingGetRequestParams();
        params.offset = offset;
        params.limit = limit;

        return params;
    }
}
