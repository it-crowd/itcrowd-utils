package pl.itcrowd.utils.config.applicationConfig;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.impl.base.filter.ExcludeRegExpPaths;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.itcrowd.utils.config.ApplicationConfigMock;
import pl.itcrowd.utils.config.InvalidConfigurationException;
import pl.itcrowd.utils.config.PBESpecMock;
import pl.itcrowd.utils.config.Setting;
import pl.itcrowd.utils.config.SettingDAO;
import pl.itcrowd.utils.config.SettingDAOMock;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.File;

@RunWith(Arquillian.class)
public class ApplicationConfigIT {

    @Inject
    private Instance<ApplicationConfigMock> applicationConfigInstance;

    @Inject
    private PBESpecMock pbeSpec;

    @Inject
    private SettingDAO settingDAO;

    @Deployment
    public static WebArchive createDeployment()
    {
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, ApplicationConfigIT.class.getSimpleName() + ".war");
        archive.addPackages(true, new ExcludeRegExpPaths(".*IT.class$"), "pl.itcrowd.utils");
        final BeansDescriptor beansDescriptor = Descriptors.create(BeansDescriptor.class)
            .getOrCreateAlternatives()
            .clazz(PBESpecMock.class.getCanonicalName())
            .up();
        archive.addAsWebInfResource(new StringAsset(beansDescriptor.exportAsString()), "beans.xml");
        archive.addClass(SettingDAOMock.class);
        archive.addClass(ApplicationConfigMock.class);
        final MavenDependencyResolver dependencyResolver = DependencyResolvers.use(MavenDependencyResolver.class)
            .loadMetadataFromPom("pom.xml")
            .artifact("commons-codec:commons-codec:1.6")
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

    @Before
    public void before()
    {
        pbeSpec.setAlgorithm("PBEWithMD5AndDES");
        pbeSpec.setIterationCount(1410);
        pbeSpec.setPassword("PaniGienia");
        pbeSpec.setSalt("a^7i#g@-v");
    }

    @Test(expected = InvalidConfigurationException.class)
    public void missingSetting()
    {
        final ApplicationConfigMock applicationConfig = applicationConfigInstance.get();
        applicationConfig.getMailPassword();
    }

    @Test
    public void sameApplicationScopeInstance()
    {
        Assert.assertEquals(applicationConfigInstance.get(), applicationConfigInstance.get());
    }

    @Test
    public void saveEncrypted()
    {
        final ApplicationConfigMock applicationConfig = applicationConfigInstance.get();
        final String paniGieniDzien = "PaniGieniDzien";
        applicationConfig.setMailPassword(paniGieniDzien);
        final Setting setting = settingDAO.load(ApplicationConfigMock.KEY.MAIL_PASSWORD.name());
        Assert.assertNotNull(setting);
        Assert.assertFalse(paniGieniDzien.equals(setting.getValue()));
        applicationConfig.reload();
        Assert.assertEquals(paniGieniDzien, applicationConfig.getMailPassword());
    }
}
