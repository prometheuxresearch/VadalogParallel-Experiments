package test.model.types;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.co.prometheux.prometheuxreasoner.model.types.IntegerMarkedNull;
import uk.co.prometheux.prometheuxreasoner.model.types.JollyMarkedNull;
import uk.co.prometheux.prometheuxreasoner.model.types.StringMarkedNull;
import uk.co.prometheux.prometheuxreasoner.model.types.TypeComparisonException;
import uk.co.prometheux.prometheuxreasoner.model.types.VadaValue;

public class TestVadaValue {

	@Test
	public void testGetValue() {
		VadaValue<Integer> i1 = new VadaValue<>(3);
		VadaValue<Integer> i2 = new VadaValue<>(3);
		VadaValue<Integer> i3 = new VadaValue<>(5);
		
		VadaValue<Integer> d1 = new VadaValue<>(3);
		VadaValue<Integer> d2 = new VadaValue<>(5);
		
		
		VadaValue<Integer> i4 = new VadaValue<>(new IntegerMarkedNull("z1"));
		VadaValue<Integer> i5 = new VadaValue<>(new IntegerMarkedNull("z1"));
		VadaValue<Integer> i6 = new VadaValue<>(new IntegerMarkedNull("z2"));
		
		VadaValue<String> s1 = new VadaValue<>("a");
		VadaValue<String> s2 = new VadaValue<>("a");
		VadaValue<String> s3 = new VadaValue<>("b");
		VadaValue<String> s4 = new VadaValue<>(new StringMarkedNull("z1"));
		VadaValue<String> s5 = new VadaValue<>(new StringMarkedNull("z1"));
		VadaValue<String> s6 = new VadaValue<>(new StringMarkedNull("z2"));
		VadaValue<String> s7 = new VadaValue<>(new StringMarkedNull("z3"));

		
		
		assert(d1.compareTo(d1)==0);
		assert(d1.compareTo(d2)<0);
		assert(d2.compareTo(d1)>0);
		
		assert(i1.compareTo(i2)==0);
		assert(i1.compareTo(i3)<0);
		assert(i3.compareTo(i2)>0);
		
		assert(s1.compareTo(s2)==0);
		assert(s1.compareTo(s3)<0);
		assert(s3.compareTo(s1)>0);
		
		assert(s4.compareTo(s5)==0);
		assert(s4.compareTo(s6)<0);
		assert(s6.compareTo(s4)>0);
		
		



		assert(i1.equals(i1));
		assert(i2.equals(i2));
		assert(i1.equals(i2));
		assert(i2.equals(i1));
		assert(!i1.equals(i3));
		assert(!i3.equals(i1));
		assert(i4.equals(i4));
		assert(i5.equals(i4));
		assert(i4.equals(i5));
		assert(!i6.equals(i5));
		assert(!i4.equals(i6));
		assert(!i5.equals(i1));
		assert(!i1.equals(i5));
		
		assert(s1.equals(s1));
		assert(s2.equals(s2));
		assert(s1.equals(s2));
		assert(s2.equals(s1));
		assert(!s1.equals(s3));
		assert(!s3.equals(s1));
		assert(s4.equals(s4));
		assert(s5.equals(s4));
		assert(s4.equals(s5));
		assert(!s6.equals(s5));
		assert(!s4.equals(s6));
		assert(!s5.equals(s1));
		assert(!s1.equals(s5));		
		assert(!s1.equals(i1));
		assert(!s6.equals(i6));
		assert(!s6.equals(s7));

		JollyMarkedNull j1 = new JollyMarkedNull("z1");
		JollyMarkedNull j2 = new JollyMarkedNull("z1");
		JollyMarkedNull j3 = new JollyMarkedNull("z2");
		
		assert(j1.equals(j2));
		assert(j1.equals(j1));
		assert(!j3.equals(j1));



	
		
		
		
	}
	
	@Test
	public void testGetValueWithSets() {
		
		Set<Integer> s0 = new HashSet<>();
		s0.add(1);
		s0.add(2);
		
		Set<Integer> s1 = new HashSet<>();
		s1.add(1);
		s1.add(2);
		s1.add(3);
		
		Set<Integer> s2 = new HashSet<>();
		s2.add(1);
		s2.add(2);
		s2.add(3);

		Set<?> s3 = new HashSet<>();
		Set<?> s4 = new HashSet<>();
		
		Set<Integer> s5 = new HashSet<>();
		s5.add(1);
		s5.add(2);
		s5.add(5);
		
		VadaValue<Set<Integer>> vs0 = new VadaValue<>(s0);
		VadaValue<Set<Integer>> vs1 = new VadaValue<>(s1);
		VadaValue<Set<Integer>> vs2 = new VadaValue<>(s2);
		VadaValue<Set<?>> vs3 = new VadaValue<>(s3);
		VadaValue<Set<?>> vs4 = new VadaValue<>(s4);
		VadaValue<Set<Integer>> vs5 = new VadaValue<>(s5);
		
		assert(vs0.equals(vs0));
		assert(!vs0.equals(vs1));
		assert(vs1.equals(vs2));
		assert(vs2.equals(vs1));
		assert(!vs2.equals(vs3));
		assert(vs3.equals(vs4));
		
		assert(vs0.compareTo(vs0)==0);
		assert(vs0.compareTo(vs1)==-1);
		assert(vs1.compareTo(vs0)==1);
		assert(vs3.compareTo(vs1)==-1);
		assert(vs1.compareTo(vs3)==1);
		
		try {
			vs2.compareTo(vs5);
			fail("Exception not thrown.");
		} catch(TypeComparisonException ex) { }
		
	}

	@Test
	public void testGetValueWithBoolean() {
		VadaValue<Boolean> vb1 = new VadaValue<>(true);
		VadaValue<Boolean> vb2 = new VadaValue<>(false);
		VadaValue<Boolean> vb3 = new VadaValue<>(true);
		
		assert(vb1.equals(vb1));
		assert(vb1.equals(vb3));
		assert(!vb1.equals(vb2));
		assert(!vb2.equals(vb3));
	}

}
