package pl.itcrowd.utils.config.applicationConfig;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.impl.base.filter.ExcludeRegExpPaths;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.itcrowd.utils.config.ApplicationConfigMock;
import pl.itcrowd.utils.config.PBESpecImpl;
import pl.itcrowd.utils.config.Setting;
import pl.itcrowd.utils.config.SettingDAO;
import pl.itcrowd.utils.config.SettingDAOMock;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;

@RunWith(Arquillian.class)
public class PBESpecImplIT {

    @Inject
    private Instance<ApplicationConfigMock> applicationConfigInstance;

    @Inject
    private PBESpecImpl pbeSpec;

    @Inject
    private SettingDAO settingDAO;

    @Deployment
    public static WebArchive createPBESpecImplDeployment()
    {
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, PBESpecImplIT.class.getSimpleName() + ".war");
        archive.addPackages(true, new ExcludeRegExpPaths(".*IT.class$"), "pl.itcrowd.utils");
        final Descriptor beansDescriptor = Descriptors.create(BeansDescriptor.class).getOrCreateAlternatives().clazz(PBESpecImpl.class.getCanonicalName()).up();
        archive.addAsWebInfResource(new StringAsset(beansDescriptor.exportAsString()), "beans.xml");
        archive.addClass(SettingDAOMock.class);
        archive.addClass(ApplicationConfigMock.class);
        final MavenDependencyResolver dependencyResolver = DependencyResolvers.use(MavenDependencyResolver.class)
            .loadMetadataFromPom("pom.xml")
            .artifact("commons-codec:commons-codec:1.6")
            .artifact("commons-lang:commons-lang:2.6")
            .artifact("pl.itcrowd.seam3.persistence-framework:seam3-persistence-framework-domain:1.1.3");
        archive.addAsLibraries(dependencyResolver.resolveAsFiles());

        final File jrebelFile = new File("target/classes/rebel.xml");
        if (jrebelFile.exists()) {
            archive.addAsResource(jrebelFile, "rebel.xml");
        } else {
            System.err.println(jrebelFile.getAbsolutePath() + " does not exist and won't be packaged");
        }

        return archive;
    }

    @Test
    public void pbeSpecImpl() throws NamingException
    {
//        Given
        final ApplicationConfigMock applicationConfig = applicationConfigInstance.get();
        pbeSpec.setAlgorithmJNDI("java:/PBESpecImplIT/algorithm");
        pbeSpec.setIterationCountJNDI("java:/PBESpecImplIT/iterationCount");
        pbeSpec.setPasswordJNDI("java:/PBESpecImplIT/password");
        pbeSpec.setSaltJNDI("java:/PBESpecImplIT/salt");
        final InitialContext initialContext = new InitialContext();
        initialContext.bind(pbeSpec.getAlgorithmJNDI(), "PBEWithMD5AndDES");
        initialContext.bind(pbeSpec.getIterationCountJNDI(), "123");
        initialContext.bind(pbeSpec.getPasswordJNDI(), "PaniGienia");
        initialContext.bind(pbeSpec.getSaltJNDI(), "^%fsd57&FDjFDSA");

//        When
        applicationConfig.setMailPassword("Wacek");
        applicationConfig.reload();
        final String mailPassword = applicationConfig.getMailPassword();
        final Setting setting = settingDAO.load(ApplicationConfigMock.KEY.MAIL_PASSWORD.name());

//        Then
        Assert.assertEquals("Wacek", mailPassword);
        Assert.assertNotNull(setting);
        Assert.assertEquals("ff26e076b8c6c488", setting.getValue());
    }
}
