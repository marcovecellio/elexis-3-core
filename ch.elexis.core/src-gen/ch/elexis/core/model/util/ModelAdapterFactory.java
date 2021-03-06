/**
 * Copyright (c) 2013 MEDEVIT <office@medevit.at>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 */
package ch.elexis.core.model.util;

import ch.elexis.core.model.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import ch.elexis.core.model.IChangeListener;
import ch.elexis.core.model.ICodeElement;
import ch.elexis.core.model.IContact;
import ch.elexis.core.model.IPatient;
import ch.elexis.core.model.IPersistentObject;
import ch.elexis.core.model.IPerson;
import ch.elexis.core.model.ISticker;
import ch.elexis.core.model.IUser;
import ch.elexis.core.model.IXid;
import ch.elexis.core.model.Identifiable;
import ch.elexis.core.model.ModelPackage;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * @see ch.elexis.core.model.ModelPackage
 * @generated
 */
public class ModelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static ModelPackage modelPackage;
	
	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ModelAdapterFactory(){
		if (modelPackage == null) {
			modelPackage = ModelPackage.eINSTANCE;
		}
	}
	
	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc
	 * --> This implementation returns <code>true</code> if the object is either the model's package
	 * or is an instance object of the model. <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object){
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}
	
	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	protected ModelSwitch<Adapter> modelSwitch = new ModelSwitch<Adapter>() {
			@Override
			public Adapter caseIContact(IContact object) {
				return createIContactAdapter();
			}
			@Override
			public Adapter caseIPersistentObject(IPersistentObject object) {
				return createIPersistentObjectAdapter();
			}
			@Override
			public Adapter caseIXid(IXid object) {
				return createIXidAdapter();
			}
			@Override
			public Adapter caseICodeElement(ICodeElement object) {
				return createICodeElementAdapter();
			}
			@Override
			public Adapter caseIChangeListener(IChangeListener object) {
				return createIChangeListenerAdapter();
			}
			@Override
			public Adapter caseISticker(ISticker object) {
				return createIStickerAdapter();
			}
			@Override
			public Adapter caseIPerson(IPerson object) {
				return createIPersonAdapter();
			}
			@Override
			public Adapter caseIPatient(IPatient object) {
				return createIPatientAdapter();
			}
			@Override
			public Adapter caseIUser(IUser object) {
				return createIUserAdapter();
			}
			@Override
			public Adapter caseIdentifiable(Identifiable object) {
				return createIdentifiableAdapter();
			}
			@Override
			public Adapter caseDeleteable(Deleteable object) {
				return createDeleteableAdapter();
			}
			@Override
			public <T> Adapter caseComparable(Comparable<T> object) {
				return createComparableAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};
	
	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target){
		return modelSwitch.doSwitch((EObject)target);
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.IContact <em>IContact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.IContact
	 * @generated
	 */
	public Adapter createIContactAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.IPersistentObject <em>IPersistent Object</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.IPersistentObject
	 * @generated
	 */
	public Adapter createIPersistentObjectAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.IXid <em>IXid</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.IXid
	 * @generated
	 */
	public Adapter createIXidAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.ICodeElement <em>ICode Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so
	 * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.ICodeElement
	 * @generated
	 */
	public Adapter createICodeElementAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.IChangeListener <em>IChange Listener</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null
	 * so that we can easily ignore cases; it's useful to ignore a case when inheritance will catch
	 * all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.IChangeListener
	 * @generated
	 */
	public Adapter createIChangeListenerAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.ISticker <em>ISticker</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.ISticker
	 * @generated
	 */
	public Adapter createIStickerAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.IPerson <em>IPerson</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.IPerson
	 * @generated
	 */
	public Adapter createIPersonAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.IPatient <em>IPatient</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.IPatient
	 * @generated
	 */
	public Adapter createIPatientAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.IUser <em>IUser</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the
	 * cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.IUser
	 * @generated
	 */
	public Adapter createIUserAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.Identifiable <em>Identifiable</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so
	 * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.Identifiable
	 * @generated
	 */
	public Adapter createIdentifiableAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link ch.elexis.core.model.Deleteable <em>Deleteable</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so
	 * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ch.elexis.core.model.Deleteable
	 * @generated
	 */
	public Adapter createDeleteableAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for an object of class '{@link java.lang.Comparable <em>Comparable</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so
	 * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all
	 * the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.lang.Comparable
	 * @generated
	 */
	public Adapter createComparableAdapter(){
		return null;
	}
	
	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc --> This default
	 * implementation returns null. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter(){
		return null;
	}
	
} // ModelAdapterFactory
