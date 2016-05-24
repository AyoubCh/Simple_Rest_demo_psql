import com.Application;
import com.model.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.toIntExact;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UnitTests {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String userName = "bdussault";

    private final String endPoint = "/persons";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Person> personList = new ArrayList<>();

    @Autowired
    private com.repositories.PersonRepository personRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    protected String jsonWithId(Object o,long id) throws IOException
    {
        String res = "{\n  \"id\":"+id+","+json(o).substring(1);
        System.out.println(res);
        return  res;
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.personRepository.deleteAllInBatch();

        this.personList.add(personRepository.save(new Person("ayoub","chioukh")));
        this.personList.add(personRepository.save(new Person("charles","moreau")));
        this.personList.add(personRepository.save(new Person("margot","piva")));
    }

    @Test
    public void createUser() throws Exception {
        Person person = new Person("toto","titi");
        mockMvc.perform(post(endPoint)
                .contentType(contentType)
                .content(json(person)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(toIntExact(this.personList.get(2).getId())+1)))
                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())));
    }

    @Test
    public void getPersonNamedTest() throws Exception {
        mockMvc.perform(get(endPoint)
                .param("name", this.personList.get(0).getFirstName())
                .contentType(contentType))

                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(toIntExact(this.personList.get(0).getId()))))
                .andExpect(jsonPath("$.firstName").value(this.personList.get(0).getFirstName()))
                .andExpect(jsonPath("$.lastName", is(this.personList.get(0).getLastName())))
                ;
    }

    @Test
    public void getPersonIdTest() throws Exception {
        mockMvc.perform(get(endPoint +"/"+ this.personList.get(1).getId()).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(toIntExact(this.personList.get(1).getId()))))
                .andExpect(jsonPath("$.firstName", is(this.personList.get(1).getFirstName())))
                .andExpect(jsonPath("$.lastName", is(this.personList.get(1).getLastName())))
                ;
    }


    @Test
    public void PersonNotFound() throws Exception {
        mockMvc.perform(get(endPoint +"/"+ 0)
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTest() throws Exception {
        Person toDelete = personList.get(2);
        mockMvc.perform(delete(endPoint +"/"+ toDelete.getId() + "/delete" )
                .contentType(contentType))
                .andExpect(status().isOk());
    }


    @Test
    public void updateTest() throws Exception {
        Person person = personList.get(0);
        mockMvc.perform(put(endPoint+"/"+person.getId())
                .contentType(contentType)
                .content(jsonWithId(person,person.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(toIntExact(person.getId()))))
                .andExpect(jsonPath("firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                ;
    }

    /*@Test
    public void readBookmarks() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.personList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$[0].description", is("A description")))
                .andExpect(jsonPath("$[1].id", is(this.personList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + userName)))
                .andExpect(jsonPath("$[1].description", is("A description")));
    }

    @Test
    public void createBookmark() throws Exception {
        String bookmarkJson = json(new Bookmark(this.account, "http://spring.io", "a bookmark to the best resource for Spring news and information"));
        this.mockMvc.perform(post("/" + userName + "/bookmarks")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isCreated());
    }*/


}