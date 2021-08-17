/*
 *  LanguageTool, a natural language style checker
 *  * Copyright (C) 2020 Fabian Richter
 *  * All rights reserved - not part of the Open Source edition
 *
 */

package org.languagetool.server;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public enum GroupRoles {
  // one user can have multiple roles
  // basic user types - each user should only have one of those
  EXISTING_MEMBER, // previously existing account, can only be removed from group, can't be deleted by owner/admin
  MEMBER, // fully managed account, deleted on removal from the group
  OWNER, // user who created group, full privileges
  // flags for privileges
  ADMIN, // full privileges
  EDITOR; // can edit dictionary

  public static final String SEPARATOR = ",";

  // encode/decode for storage in database
  public static String encode(List<GroupRoles> roles) {
    String role = roles.stream().map(GroupRoles::name).collect(Collectors.joining(GroupRoles.SEPARATOR));
    return role;
  }

  public static List<GroupRoles> decode(String value) {
    String[] roleValues = value.split(GroupRoles.SEPARATOR);
    return Arrays.stream(roleValues).map(GroupRoles::valueOf).collect(Collectors.toList());
  }

  public static boolean hasPermissions(@Nullable String role, GroupRoles... roles) {
    if (role == null) {
      return false;
    }
    HashSet<GroupRoles> userRoles = Sets.newHashSet(decode(role));
    HashSet<GroupRoles> requiredRoles = Sets.newHashSet(Arrays.asList(roles));
    return !Sets.intersection(userRoles, requiredRoles).isEmpty();
  }
}
