/*
 * $RCSfile: Vector3f.java,v $
 *
 * Copyright 1997-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 *
 * $Revision: 1.5 $
 * $Date: 2008/02/28 20:18:51 $
 * $State: Exp $
 */

package javax.vecmath;

import java.lang.Math;

/**
 * A 3-element vector that is represented by single-precision floating point 
 * x,y,z coordinates.  If this value represents a normal, then it should
 * be normalized.
 *
 */
public class Vector3f extends Tuple3f implements java.io.Serializable {

	// Combatible with 1.1
	static final long serialVersionUID = -7031930069184524614L;

	public static final Vector3f X = new Vector3f(1, 0, 0);
	public static final Vector3f XN = new Vector3f(-1, 0, 0);
	public static final Vector3f Y = new Vector3f(0, 1, 0);
	public static final Vector3f YN = new Vector3f(0, -1, 0);
	public static final Vector3f Z = new Vector3f(0, 0, 1);
	public static final Vector3f ZN = new Vector3f(0, 0, -1);

	/**
	 * Constructs and initializes a Vector3f from the specified xyz coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public Vector3f(float x, float y, float z)
	{
		super(x,y,z);
	}


	/**
	 * Constructs and initializes a Vector3f from the array of length 3.
	 * @param v the array of length 3 containing xyz in order
	 */
	public Vector3f(float[] v)
	{
		super(v);
	}


	/**
	 * Constructs and initializes a Vector3f from the specified Vector3f.
	 * @param v1 the Vector3f containing the initialization x y z data
	 */
	public Vector3f(Vector3f v1)
	{
		super(v1);
	}


	/**
	 * Constructs and initializes a Vector3f from the specified Vector3d.
	 * @param v1 the Vector3d containing the initialization x y z data
	 */
	public Vector3f(Vector3d v1)
	{
		super(v1);
	}


	/**
	 * Constructs and initializes a Vector3f from the specified Tuple3f.
	 * @param t1 the Tuple3f containing the initialization x y z data
	 */  
	public Vector3f(Tuple3f t1) {
		super(t1);
	}


	/**
	 * Constructs and initializes a Vector3f from the specified Tuple3d.
	 * @param t1 the Tuple3d containing the initialization x y z data
	 */  
	public Vector3f(Tuple3d t1) {
		super(t1);
	}


	/**
	 * Constructs and initializes a Vector3f to (0,0,0).
	 */
	public Vector3f()
	{
		super();
	}


	/**
	 * Returns the squared length of this vector.
	 * @return the squared length of this vector
	 */
	public final float lengthSquared()
	{
		return (this.x*this.x + this.y*this.y + this.z*this.z);
	}

	/**
	 * Returns the length of this vector.
	 * @return the length of this vector
	 */
	public final float length()
	{
		return (float) Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}


	/**
	 * Sets this vector to be the vector cross product of vectors v1 and v2.
	 * @param v1 the first vector
	 * @param v2 the second vector
	 */
	public final void cross(Vector3f v1, Vector3f v2)
	{
		float x,y;

		x = v1.y*v2.z - v1.z*v2.y;
		y = v2.x*v1.z - v2.z*v1.x;
		this.z = v1.x*v2.y - v1.y*v2.x;
		this.x = x;
		this.y = y;
	}

	/**
	 * Computes the dot product of this vector and vector v1.
	 * @param v1 the other vector
	 * @return the dot product of this vector and v1
	 */
	public final float dot(Vector3f v1)
	{
		return (this.x*v1.x + this.y*v1.y + this.z*v1.z);
	}

	/**
	 * Sets the value of this vector to the normalization of vector v1.
	 * @param v1 the un-normalized vector
	 */
	public final void normalize(Vector3f v1)
	{
		float norm;

		norm = (float) (1.0/Math.sqrt(v1.x*v1.x + v1.y*v1.y + v1.z*v1.z));
		this.x = v1.x*norm;
		this.y = v1.y*norm;
		this.z = v1.z*norm;
	}

	/**
	 * Normalizes this vector in place.
	 */
	public final void normalize()
	{
		float norm;

		norm = (float) (1.0/Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z));
		this.x *= norm;
		this.y *= norm;
		this.z *= norm;
	}

	/** 
	 *	Returns the angle in radians between this vector and the vector
	 *	parameter; the return value is constrained to the range [0,PI]. 
	 *	@param v1	the other vector 
	 *	@return	the angle in radians in the range [0,PI] 
	 */	
	public final float angle(Vector3f v1) 
	{ 
		double vDot = this.dot(v1) / ( this.length()*v1.length() );
		if( vDot < -1.0) vDot = -1.0;
		if( vDot >  1.0) vDot =  1.0;
		return((float) (Math.acos( vDot )));
	}

	public final float angle(){
		return angle(new Vector3f(1, 0, 0));
	}

	/**
	 * BDX conveniences:
	 */
	
	public final void scale(Vector3f v){
		x *= v.x;
		y *= v.y;
		z *= v.z;
	}
	
	public final Vector3f crossed(Vector3f other) {
		Vector3f result = new Vector3f();
		result.cross(this, other);
		return result;
	}

	public final void cross(Vector3f other) {
		this.set(crossed(other));
	}

	public final Vector3f rotated(Vector3f axis, float radians) {
		return Matrix3f.rotation(axis, radians).mult(this);
	}

	public final void rotate(Vector3f axis, float radians) {
		this.set(rotated(axis, radians));
	}

	public final Vector3f normalized(){
		Vector3f a = new Vector3f(this);
		a.normalize();
		return a;
	}

	public final Vector3f plus(Vector3f b){
		Vector3f a = new Vector3f(this);
		a.add(b);
		return a;
	}

	public final Vector3f plus(float x, float y, float z) {
		Vector3f a = new Vector3f(this);
		a.add(x, y, z);
		return a;
	}

	public final Vector3f minus(Vector3f b){
		Vector3f a = new Vector3f(this);
		a.sub(b);
		return a;
	}

	public final Vector3f minus(float x, float y, float z) {
		Vector3f a = new Vector3f(this);
		a.sub(x, y, z);
		return a;
	}

	public final void length(float n){
		if (length() == 0)
			return;
		normalize();
		scale(n);
	}

	public final Vector3f negated(){
		Vector3f v = new Vector3f(this);
		v.negate();
		return v;
	}

	public final Vector3f mul(float n){
		Vector3f v = new Vector3f(this);
		v.x *= n;
		v.y *= n;
		v.z *= n;
		return v;
	}

	public final Vector3f mul(Vector3f b){
		Vector3f a = new Vector3f(this);
		a.x *= b.x;
		a.y *= b.y;
		a.z *= b.z;
		return a;
	}

	public final Vector3f div(Vector3f b){
		Vector3f a = new Vector3f(this);
		a.x /= b.x;
		a.y /= b.y;
		a.z /= b.z;
		return a;
	}

	public final Vector3f reflected(Vector3f normal){
		return normal.mul(-2 * this.dot(normal)).plus(this);  
	}

}
