package io.jenkins.plugins.configuration;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.model.Item;
import hudson.security.ACL;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *@author selvavignesh.m
 * @version 1.0
 */
public class SprintsConnection {
    private static final Logger LOGGER = Logger.getLogger(SprintsConnection.class.getName());
    private final String name;
    private final String url;
    private transient String apiToken;
    private String apiTokenId;
    private SprintsConfig config;
    private final String mailid;


    /**
     *
     * @param name Name of the Connection
     * @param url Sprints Portal Url
     * @param mailid Admin Mail id
     * @param apiTokenId Selected apitoken id from SprintsGlobalConf
     */
    @DataBoundConstructor
    public SprintsConnection(String name, String url, String mailid, String apiTokenId ) {
        this.name = name;
        this.url = url;
        this.mailid = mailid;
        this.apiTokenId = apiTokenId;
    }

    /**
     *
     * @return String
     */
    public String getMailid() {
        return mailid;
    }
    /**
     *
     * @return String
     */
    public String getName() {
        return name;
    }
    /**
     *
     * @return String
     */
    public String getUrl() {
        return url;
    }
    /**
     *
     * @return String
     */
    public String getApiTokenId() {
        return apiTokenId;
    }
    /**
     *
     * @return SprintsConfig
     */
    public SprintsConfig getClient() {
        if (config == null) {
            config = new SprintsConfig(url, mailid, getApiToken(apiTokenId));
        }
        return config;
    }

    /**
     *
     * @param apiTokenId apiToken from Sprints Global config to get apikey value
     * @return String
     */
    @Restricted(NoExternalUse.class)
    private String getApiToken(String apiTokenId) {
        StandardCredentials credentials = CredentialsMatchers.firstOrNull(
                CredentialsProvider.lookupCredentials(StandardCredentials.class, (Item) null, ACL.SYSTEM, new ArrayList<DomainRequirement>()),
                CredentialsMatchers.withId(apiTokenId));
        if (credentials != null) {
            if (credentials instanceof SprintsApiToken) {
                return ((SprintsApiToken) credentials).getApiToken().getPlainText();
            }
            if (credentials instanceof StringCredentials) {
                return ((StringCredentials) credentials).getSecret().getPlainText();
            }
        }
        throw new IllegalStateException("No credentials found for credentialsId: " + apiTokenId);
    }


}
