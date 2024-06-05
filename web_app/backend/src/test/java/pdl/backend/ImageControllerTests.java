package pdl.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	public static void reset() {
		// reset Image class static counter
		ReflectionTestUtils.setField(Image.class, "count", Long.valueOf(0));
	}

	@Test
	@Order(1)
	public void postLoginReturnSucces() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
		.param("user", "test")
		.param("pwd", "123")
).andExpect(status().isOk());
	}


	@Test
	@Order(2)
	public void getImageListShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(get("/test/images")).andExpect(status().isOk());
	}

	@Test
	@Order(3)
	public void getImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(get("/test/images/5000")).andExpect(status().isNotFound());
	}

	@Test
	@Order(4)
	public void getImageShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(get("/test/images/0")).andExpect(status().isOk());
	}

	@Test
	@Order(5)
	public void deleteImagesShouldReturnMethodNotAllowed() throws Exception {
		this.mockMvc.perform(delete("/test/images")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	@Order(6)
	public void deleteImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(delete("/test/images/500")).andExpect(status().isNotFound());
	}

	@Test
	@Order(7)
	public void deleteImageShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(delete("/test/images/0")).andExpect(status().isOk());
	}

	@Test
	@Order(8)
	public void createImageShouldReturnSuccess() throws Exception {
		final ClassPathResource img_path = new ClassPathResource("lib/test/flower.jpg");
		MockMultipartFile img = new MockMultipartFile("file", "flower.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				Files.readAllBytes(img_path.getFile().toPath()));
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/test/images").file(img)).andExpect(status().isOk());
	}

	@Test
	@Order(9)
	public void createImageShouldReturnUnsupportedMediaType() throws Exception {
		final ClassPathResource img_path = new ClassPathResource("lib/test/flower.jpg");
		MockMultipartFile img = new MockMultipartFile("file", "flower.jpg", MediaType.IMAGE_GIF_VALUE,
				Files.readAllBytes(img_path.getFile().toPath()));
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/test/images").file(img))
				.andExpect(status().isUnsupportedMediaType());
	}

	@Test
	@Order(10)
	public void getLumGetOk() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=lum&val=100")).andExpect(status().isOk());
	}

	@Test
	@Order(11)
	public void getLumGetWithoutArgument() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=lum")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(12)
	public void getLumGetWrongArgument() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=lum&val=abc")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(13)
	public void getHeqGetOk() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=heq&chan=s")).andExpect(status().isOk());
	}

	@Test
	@Order(14)
	public void getHeqGetWithoutParam() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=heq")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(15)
	public void getHeqGetWrongParam() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=heq&chan=2")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(16)
	public void getHueGetOk() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=hue&col=100")).andExpect(status().isOk());
	}

	@Test
	@Order(17)
	public void getHueGetWithoutParam() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=hue")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(18)
	public void getHueGetWrongParam() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=hue&col=abc")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(19)
	public void getBlurGetOk() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=blur&method=g")).andExpect(status().isOk());
	}

	@Test
	@Order(20)
	public void getBlurGetWithoutParam() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=blur")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(21)
	public void getBlurGetWrongParam() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=blur&method=5")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(22)
	public void getEdgeGetOk() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=edge")).andExpect(status().isOk());
	}

	@Test
	@Order(23)
	public void getWrongAlgo() throws Exception {
		this.mockMvc.perform(get("/test/images/1?algorithm=lol")).andExpect(status().isBadRequest());
	}

	@Test
	@Order(24)
	public void logoutShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(delete("/logout/badtest")).andExpect(status().isNotFound());
	}

	@Test
	@Order(24)
	public void logoutShouldReturnSucces() throws Exception {
		this.mockMvc.perform(delete("/logout")
		.param("user","test")).andExpect(status().isOk());
	}

	@Test
	@Order(25)
	public void postLoginReturnError() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/login")
		.param("user","test")
		.param("pwd","321")).andExpect(status().isUnauthorized());
	}

	@Test
	@Order(26)
	public void postLoginShouldReturnError() throws Exception{
		this.mockMvc.perform(MockMvcRequestBuilders.multipart("/login")
		.param("user","faux")
		.param("pwd","321")).andExpect(status().isUnauthorized());
	}
}
